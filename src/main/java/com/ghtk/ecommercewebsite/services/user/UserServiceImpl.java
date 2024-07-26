package com.ghtk.ecommercewebsite.services.user;

import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.TokenRepository;
import com.ghtk.ecommercewebsite.services.JwtService;
import com.ghtk.ecommercewebsite.services.auth.AuthenticationService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.Role;
import com.ghtk.ecommercewebsite.models.entities.Users;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.repositories.RoleRepository;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationService authenticationService;
//    private final EmailService emailService;

    @Override
    @Transactional
    public Users signUp(RegisterUserDto input) throws UserAlreadyExistedException {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Role userRole = optionalRole.get();

        Optional<Users> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
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
            var user = Users.builder()
                    .fullName(input.getFullName())
                    .email(input.getEmail())
                    .password(passwordEncoder.encode(input.getPassword()))
                    .roles(roles)
                    .build();
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
    public Users getAuthenticatedUser() {
        return (Users) authenticationService.getAuthentication().getPrincipal();
    }

    @Override
    public List<Users> allUsers() {
        List<Users> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    @Override
    public List<Users> allSellers() {
        return userRepository.findByRolesContaining(RoleEnum.SELLER);
    }

    @Override
    public Users getUserDetailsFromToken(String token) throws Exception {
        String username = jwtService.extractUsername(token);
        Optional<Users> user;
        user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public Users getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

}
