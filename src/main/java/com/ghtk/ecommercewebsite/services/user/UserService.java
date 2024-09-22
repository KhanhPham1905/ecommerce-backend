package com.ghtk.ecommercewebsite.services.user;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.dtos.UserDTO;
import com.ghtk.ecommercewebsite.models.dtos.UserProfileDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService {
    User signUp(RegisterUserDto input);
    LoginResponse authenticateUserAndGetLoginResponse(LoginUserDto loginUserDto);
    User getAuthenticatedUser();
    Page<User> allUsers(Pageable pageable);
    List<User> findAllSellers();
    Page<User> allSellers(Pageable pageable);
    User getUserDetailsFromToken(String token);
    User getUserDetailsFromRefreshToken(String refreshToken);
    User viewDetailsOfAnUser(Long id);

    User updateUserInfo(UserDTO userDTO);

    String signUpWithOtp(RegisterUserDto registerUserDto);

    void checkUserExistence(RegisterUserDto registerUserDto);
    void sendMailForSignUpUser(RegisterUserDto registerUserDto);

    User signUpNewVersion(RegisterUserDto registerUserDto);

    void activateUser(String email);

    UserProfileDTO getUserProfile();

    User updateUserProfile(UserProfileDTO userProfileDTO);

    User sendMail(String email);

    void addSellerRole(String email);

    String sendOtpForForgotPasswordRequest(String email);
}
