package com.ghtk.ecommercewebsite.services.user;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.MailBody;
import com.ghtk.ecommercewebsite.models.dtos.UserDTO;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.*;
import com.ghtk.ecommercewebsite.services.EmailService;
import com.ghtk.ecommercewebsite.services.JwtService;
import com.ghtk.ecommercewebsite.services.RedisOtpService;
import com.ghtk.ecommercewebsite.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationService authenticationService;
    private final RedisOtpService redisOtpService;
    private final EmailService emailService;
//    private final LocationRepository locationRepository;
//    private final AddressRepository addressRepository;
//    private final EmailService emailService;
//    public static final String KEY = "cacheKey";

    @Override
    @Transactional
    @CacheEvict(value = "sellers", allEntries = true)
    public User signUp(RegisterUserDto input) throws UserAlreadyExistedException {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Role userRole = optionalRole.get();

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();
//            Role userRole = Role.builder().name(RoleEnum.USER).build();

            if (existingRoles.contains(userRole)) {
                throw new UserAlreadyExistedException(input.getEmail());
            } else {
                existingRoles.add(userRole);
                existingUser.setRoles(existingRoles);
//                sendMail(input.getEmail());
                return userRepository.save(existingUser);
            }
        } else {
            Set<Role> roles = new HashSet<>(List.of(optionalRole.get()));
            var user = User.builder()
                    .fullName(input.getFullName())
                    .email(input.getEmail())
                    .password(passwordEncoder.encode(input.getPassword()))
                    .phone(input.getPhone())
                    .gender(input.getGender())
                    .roles(roles)
                    .build();
            // From here
//            Optional<Location> location = locationRepository.findByCountryAndProvinceAndDistrictAndCommune(input.getCountry(), input.getProvince(), input.getDistrict(), input.getCommune());
//            if (location.isPresent()) {
//                Location existedLocation = location.get();
//                Address newAddressWithDetail = Address.builder()
//                        .locationId(existedLocation.getId())
//                        .addressDetail(input.getAddressDetail())
//                        .build();
//                addressRepository.save(newAddressWithDetail);
//                user.setAddressId(newAddressWithDetail.getId());
//            } else {
//                Location newLocation = Location.builder()
//                        .country(input.getCountry())
//                        .province(input.getProvince())
//                        .district(input.getDistrict())
//                        .commune(input.getCommune())
//                        .build();
//                locationRepository.save(newLocation);
//                Address newAddressWithDetail = Address.builder()
//                        .locationId(newLocation.getId())
//                        .addressDetail(input.getAddressDetail())
//                        .build();
//                user.setAddressId(newAddressWithDetail.getId());
//            }
            // to here is for new address implementation
//            sendMail(input.getEmail());
            return userRepository.save(user);
        }
    }

//    private void sendMail(String mail) {
//        emailService.sendSimpleMessage(mail,
//                "Signed up successfully",
//                "You have just created an account with " + mail);
//    }

    @Override
    public LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateUserAndGetLoginResponse(loginUserDto);
    }

    @Override
    public User getAuthenticatedUser() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }

//    @Override
//    public List<User> allUsers() {
//        List<User> users = new ArrayList<>();
//        userRepository.findAll().forEach(users::add);
//        return users;
//    }

//    @Override
//    public List<User> allSellers() {
//        return userRepository.findByRolesContaining(RoleEnum.SELLER);
//    }

    @Override
//    @Cacheable(value = "sellers")
    public List<User> findAllSellers() {
        Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.SELLER);
        if (roleOptional.isPresent()) {
            return userRepository.findByRolesContaining(roleOptional.get().getName());
        } else {
            return Collections.emptyList();
        }
    }

    private Page<User> convertListToPage(List<User> sellers, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<User> pagedSellers;

        if (sellers.size() < startItem) {
            pagedSellers = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, sellers.size());
            pagedSellers = sellers.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedSellers, pageable, sellers.size());
    }

    // From controller
    @Override
    public Page<User> allSellers(Pageable pageable) {
        List<User> sellers = findAllSellers();
        return convertListToPage(sellers, pageable);
    }

    @Override
    public Page<User> allUsers(Pageable pageable) {
        Optional<Role> roleOptional = roleRepository.findByName(RoleEnum.USER);
        if (roleOptional.isPresent()) {
            return userRepository.findByRolesContaining(roleOptional.get(), pageable);
        } else {
            return Page.empty();
        }
    }

    @Override
    public User getUserDetailsFromToken(String token) throws Exception {
        String username = jwtService.extractUsername(token);
        Optional<User> user;
        user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

    @Override
    public User viewDetailsOfAnUser(Long id) throws DataNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new DataNotFoundException("There is no user with this id");
        } else {
            return user.get();
        }
//        return user.map(value -> UserDTO.builder()
//                .fullName(value.getFullName())
//                .email(value.getEmail())
//                .phone(value.getPhone())
//                .gender(value.getGender())
//                .build()).orElse(null);
    }

    @Override
    public User updateUserInfo(UserDTO userDTO) throws DataNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByEmail(userDTO.getEmail());

        if (optionalUser.isEmpty()) {
            throw new DataNotFoundException("There is no user with this email");
        }

        User user = optionalUser.get();
        user.setFullName(userDTO.getFullName());
        user.setPhone(userDTO.getPhone());
        user.setGender(userDTO.getGender());
        return userRepository.save(user);
    }

    @Override
    public String signUpWithOtp(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Role userRole = optionalRole.get();

        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();
            if (existingRoles.contains(userRole)) {
                throw new UserAlreadyExistedException(registerUserDto.getEmail());
            } else {
                existingRoles.add(userRole);
                existingUser.setRoles(existingRoles);
//                sendMail(input.getEmail());
                Integer otp = redisOtpService.generateAndSaveOtp(registerUserDto.getEmail());
                String roleNames = String.valueOf(existingRoles.stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()));
                MailBody mailBody = MailBody.builder()
                        .to(registerUserDto.getEmail())
                        .text("You are already an " + roleNames + " in our system. This is the OTP for your request: " + otp)
                        .build();
                emailService.sendSimpleMessage(mailBody);
            }
        } else {
            Set<Role> roles = new HashSet<>(List.of(optionalRole.get()));
            var user = User.builder()
                    .fullName(registerUserDto.getFullName())
                    .email(registerUserDto.getEmail())
                    .password(passwordEncoder.encode(registerUserDto.getPassword()))
                    .phone(registerUserDto.getPhone())
                    .gender(registerUserDto.getGender())
                    .roles(roles)
                    .build();
        }
        return null;
    }

    @Override
    @Transactional
    public User signUpNewVersion(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Role userRole = optionalRole.get();
        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());

        if (optionalUser.isPresent()) {
            // If user is already existing
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();

            if (existingRoles.contains(userRole)) {
                throw new UserAlreadyExistedException(registerUserDto.getEmail());
            } else {
                existingRoles.add(userRole);
                existingUser.setRoles(existingRoles);
                existingUser.setPassword(registerUserDto.getPassword());
                existingUser.setFullName(registerUserDto.getFullName());
                existingUser.setPhone(registerUserDto.getPhone());
                existingUser.setGender(registerUserDto.getGender());
                existingUser.setStatus(false);
                // Save as inactive account
                userRepository.save(existingUser);

                Integer otp = redisOtpService.generateAndSaveOtp(registerUserDto.getEmail());
                String roleNames = String.valueOf(existingRoles.stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList()));
                MailBody mailBody = MailBody.builder()
                        .to(registerUserDto.getEmail())
                        .text("You are already an " + roleNames + " in our system. This is the OTP for your request: " + otp)
                        .build();
                emailService.sendSimpleMessage(mailBody);

                return existingUser;

            }
        } else {
            Set<Role> roles = new HashSet<>(List.of(optionalRole.get()));
            var user = User.builder()
                    .fullName(registerUserDto.getFullName())
                    .email(registerUserDto.getEmail())
                    .password(passwordEncoder.encode(registerUserDto.getPassword()))
                    .phone(registerUserDto.getPhone())
                    .gender(registerUserDto.getGender())
                    .status(false)
                    .roles(roles)
                    .build();

            userRepository.save(user);
            Integer otp = redisOtpService.generateAndSaveOtp(registerUserDto.getEmail());
            MailBody mailBody = MailBody.builder()
                    .to(registerUserDto.getEmail())
                    .text("This is the OTP for your request: " + otp)
                    .build();
            emailService.sendSimpleMessage(mailBody);

            return user;
        }
    }

    @Override
    public void activateUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(true);
            userRepository.save(user);
        }
    }


    // Better approach
    @Override
    public void sendMailForSignUpUser(RegisterUserDto registerUserDto) {
//        Optional<User> existingUser = userRepository.findByEmail(registerUserDto.getEmail());
//        User userToAddUserRole = existingUser.orElseGet(User::new);

        Integer otp = redisOtpService.generateAndSaveOtp(registerUserDto.getEmail());
        MailBody mailBody = MailBody.builder()
                .to(registerUserDto.getEmail())
                .text("Hi! Please use this OTP for user signing up request: " + otp)
                .build();
        emailService.sendSimpleMessage(mailBody);
        // I think there are some problems with this process
        redisOtpService.storeTemporaryUser(registerUserDto);
    }

    @Override
    public void checkUserExistence(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return; }
        Role userRole = optionalRole.get();

        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();
            if (existingRoles.contains(userRole)) {
                throw new UserAlreadyExistedException(registerUserDto.getEmail());
            }
        }
    }
}
