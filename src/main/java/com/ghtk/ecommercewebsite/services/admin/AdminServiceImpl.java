package com.ghtk.ecommercewebsite.services.admin;

import com.ghtk.ecommercewebsite.services.auth.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.entities.Users;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AuthenticationServiceImpl authenticationService;

    @Override
    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException {
        return authenticationService.authenticateAdminAndGetLoginResponse(loginUserDto);
    }

    @Override
    public Users getAuthenticatedAdmin() {
        return (Users) authenticationService.getAuthentication().getPrincipal();
    }
}
