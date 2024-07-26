package com.ghtk.ecommercewebsite.services.auth;

import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.Users;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.security.core.Authentication;

import java.nio.file.AccessDeniedException;

public interface AuthenticationService {
    Authentication getAuthentication();
    Users authenticateByRole(LoginUserDto loginUserDto, String role) throws AccessDeniedException;
    LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    LoginResponse authenticateAdminAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    LoginResponse buildLoginResponse(Users authenticatedUser);
    boolean checkValidEmail(RegisterUserDto registerUserDto);
}
