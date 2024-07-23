package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.common.api.CommonResult;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.services.AdminService;
import com.ghtk.ecommercewebsite.services.AuthenticationService;
import com.ghtk.ecommercewebsite.services.JwtService;
import com.ghtk.ecommercewebsite.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AdminController {

    private final UserService userService;
    private final AdminService adminService;

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return CommonResult.success(adminService.authenticateAdminAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> authenticatedAdmin() {
        return CommonResult.success(adminService.getAuthenticatedAdmin());
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<User>> allSellers() {
        return CommonResult.success(userService.allSellers());
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<List<User>> allUsers() {
        return CommonResult.success(userService.allUsers());
    }

}
