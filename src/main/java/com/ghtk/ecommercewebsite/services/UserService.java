package com.ghtk.ecommercewebsite.services;

import com.ghtk.ecommercewebsite.exceptions.ExpiredTokenException;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.Role;
import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.repositories.RoleRepository;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final com.ghtk.ecommercewebsite.services.AuthenticationService authenticationService;

    @Transactional
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
                return userRepository.save(existingUser);
            }
        } else {
            Set<Role> roles = new HashSet<>(List.of(optionalRole.get()));
            var user = User.builder()
                    .fullName(input.getFullName())
                    .email(input.getEmail())
                    .password(passwordEncoder.encode(input.getPassword()))
                    .roles(roles)
                    .build();
            return userRepository.save(user);
        }
    }

    public LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateUserAndGetLoginResponse(loginUserDto);
    }

    public User getAuthenticatedUser() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public List<User> allSellers() {
        return userRepository.findByRolesContaining(RoleEnum.SELLER);
    }

    public User getUserDetailsFromToken(String token) throws Exception {
        String username = jwtService.extractUsername(token);
        Optional<User> user;
        user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new Exception("User not found"));
    }

    public User getUserDetailsFromRefreshToken(String refreshToken) throws Exception {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

}
