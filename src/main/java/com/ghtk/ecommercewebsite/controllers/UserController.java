package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.common.api.CommonResult;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.services.AuthenticationService;
import com.ghtk.ecommercewebsite.services.JwtService;
import com.ghtk.ecommercewebsite.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CommonResult<User> signup(@RequestBody RegisterUserDto registerUserDto) throws UserAlreadyExistedException {
        User user = userService.signUp(registerUserDto);
        return CommonResult.success(user);
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) throws AccessDeniedException {

        LoginResponse loginResponse = userService.authenticateUserAndGetLoginResponse(loginUserDto);

        // add cookie after login
        Cookie cookie = new Cookie("JWT_TOKEN", loginResponse.getToken());
        cookie.setMaxAge(84600);
        cookie.setPath("/");
        response.addCookie(cookie);
        return CommonResult.success(loginResponse);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public CommonResult<User> authenticatedUser() {
        return CommonResult.success(userService.getAuthenticatedUser());
    }
}