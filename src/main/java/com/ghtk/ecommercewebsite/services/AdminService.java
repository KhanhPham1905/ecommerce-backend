package com.ghtk.ecommercewebsite.services;

import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AuthenticationService authenticationService;

    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateAdminAndGetLoginResponse(loginUserDto);
    }

    public User getAuthenticatedAdmin() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }
}
