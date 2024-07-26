package com.ghtk.ecommercewebsite.services.user;

import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User signUp(RegisterUserDto input) throws UserAlreadyExistedException;
    LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    User getAuthenticatedUser();
    List<User> allUsers();
    List<User> allSellers();
    User getUserDetailsFromToken(String token) throws Exception;
    User getUserDetailsFromRefreshToken(String refreshToken) throws Exception;
}
