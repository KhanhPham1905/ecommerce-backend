package com.ghtk.ecommercewebsite.services;

import com.ghtk.ecommercewebsite.models.enums.RoleEnum;
import com.ghtk.ecommercewebsite.repositories.SellerRepository;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
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

@Service
@RequiredArgsConstructor
public class SellerService {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @Transactional
    public User signUpSeller(RegisterUserDto input) throws SellerAlreadyExistedException {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SELLER);
        if (optionalRole.isEmpty()) { return null; }

        Role sellerRole = optionalRole.get();

        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Set<Role> existingRoles = existingUser.getRoles();
//            Role sellerRole = Role.builder().name(RoleEnum.SELLER).build();

            if (existingRoles.contains(sellerRole)) {
                throw new SellerAlreadyExistedException(input.getEmail());
            } else {
                existingRoles.add(sellerRole);
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

    public LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateSellerAndGetLoginResponse(loginUserDto);
    }

    public User getAuthenticatedSeller() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }
}
