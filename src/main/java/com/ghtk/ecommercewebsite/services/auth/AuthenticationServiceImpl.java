package com.ghtk.ecommercewebsite.services.auth;

import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.services.JwtService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User authenticateByRole(LoginUserDto loginUserDto, String role) throws AccessDeniedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role))) {
            User user = userRepository.findByEmail(loginUserDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!user.getStatus()) {
                throw new AccessDeniedException("User is not activated");
            }
            return user;
//            return userRepository.findByEmail(loginUserDto.getEmail())
//                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } else {
            throw new AccessDeniedException("User does not have the required role (" + role + ")");
        }
    }

    @Override
    public LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "USER");
        return buildLoginResponse(authenticatedUser);
    }

    @Override
    public LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "SELLER");
        return buildLoginResponse(authenticatedUser);
    }

    @Override
    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        User authenticatedUser = authenticateByRole(loginUserDto, "ADMIN");
        return buildLoginResponse(authenticatedUser);
    }

    @Override
    public LoginResponse buildLoginResponse(User authenticatedUser) {
        String jwtToken = jwtService.generateToken(authenticatedUser);

        return LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }

    @Override
    public boolean checkValidEmail(RegisterUserDto registerUserDto) {
        String emailPattern = ".*@gmail\\.com$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(registerUserDto.getEmail());

        return matcher.matches();
    }

}
