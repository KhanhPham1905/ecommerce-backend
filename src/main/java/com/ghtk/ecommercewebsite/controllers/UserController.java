package com.ghtk.ecommercewebsite.controllers;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.models.dtos.RefreshTokenDTO;
import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.services.OtpService;
import com.ghtk.ecommercewebsite.services.RedisOtpService;
import com.ghtk.ecommercewebsite.services.token.TokenService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final TokenService tokenService;
    private final RedisOtpService redisOtpService;
    private final OtpService otpService;

    @PostMapping("/signup")
    public CommonResult<User> signup(@RequestBody @Valid RegisterUserDto registerUserDto) throws UserAlreadyExistedException {
        User user = userService.signUp(registerUserDto);
        return CommonResult.success(user);
    }



    // New version start from here
    @PostMapping("/signUpNewVersion")
    public CommonResult<User> verifyOtpForSigningUp(@RequestBody RegisterUserDto registerUserDto) {
        return CommonResult.success(userService.signUpNewVersion(registerUserDto));
    }
    // New version here
    @PostMapping("/verifyOtp")
    public CommonResult<String> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        Integer otp = Integer.parseInt(requestBody.get("otp"));
        boolean isOtpValid = redisOtpService.verifyOtp(email, otp);
        if (!isOtpValid) { return CommonResult.failed("Invalid OTP"); }
        userService.activateUser(email);
        return CommonResult.success("User activated successfully");
    }
    // New version here
    @PostMapping("/resendOtp")
    public CommonResult<String> resendOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return otpService.resendOtpForSigningUp(email);
    }

//    @PostMapping("/signUpWithOtp")
//    public CommonResult<String> signUpWithOtp(@RequestBody @Valid RegisterUserDto registerUserDto) {
//        userService.checkUserExistence(registerUserDto);
//        userService.sendMailForSignUpUser(registerUserDto);
//        return CommonResult.success("OTP sent successfully. Please check your email.");
//    }


//    public CommonResult<String> verifyOtpForSigningUp(@RequestBody @Valid String email) {
//
//    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws Exception {
        String token = userService.authenticateUserAndGetLoginResponse(loginUserDto).getToken();
        User userDetail = userService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token);

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
          return CommonResult.success(loginResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CommonResult<User> authenticatedUser() {
//        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("heheheeeeeeeeeeeeee" + authentication1);
        return CommonResult.success(userService.getAuthenticatedUser());
    }


    @PostMapping("/refreshToken")
    public CommonResult<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenDTO refreshTokenDTO
            ) throws  Exception {

        User userDetail = userService.getUserDetailsFromRefreshToken(refreshTokenDTO.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenDTO.getRefreshToken(), userDetail);
        LoginResponse loginResponse = LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
        return CommonResult.success(loginResponse);
    }
}