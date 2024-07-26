package com.ghtk.ecommercewebsite.services.user;

import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.Users;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    Users signUp(RegisterUserDto input) throws UserAlreadyExistedException;
    LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    Users getAuthenticatedUser();
    List<Users> allUsers();
    List<Users> allSellers();
    Users getUserDetailsFromToken(String token) throws Exception;
    Users getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
}
