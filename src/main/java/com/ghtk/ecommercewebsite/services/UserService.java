package com.ghtk.ecommercewebsite.services;

import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
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

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.ghtk.ecommercewebsite.services.AuthenticationService authenticationService;

    public User signUp(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty()) { return null; }

        Optional<User> optionalUser = userRepository.findByEmail(registerUserDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistedException(registerUserDto.getEmail());
        }

        var user = User.builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(optionalRole.get())
                .build();
        return userRepository.save(user);
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
        return userRepository.findByRoleName(RoleEnum.SELLER);
    }

    public User createSeller(RegisterUserDto registerUserDto) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SELLER);
        if (optionalRole.isEmpty()) { return null; }

        var user = User
                .builder()
                .fullName(registerUserDto.getFullName())
                .email(registerUserDto.getEmail())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .role(optionalRole.get())
                .build();
        return userRepository.save(user);
    }
}
