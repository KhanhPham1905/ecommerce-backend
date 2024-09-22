package com.ghtk.ecommercewebsite.services.admin;

import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.services.auth.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final AuthenticationServiceImpl authenticationService;

    @Override
    public LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto){
        return authenticationService.authenticateAdminAndGetLoginResponse(loginUserDto);
    }

    @Override
//    @Cacheable(value = "admin")
    public User getAuthenticatedAdmin() {
        return (User) authenticationService.getAuthentication().getPrincipal();
    }
}
