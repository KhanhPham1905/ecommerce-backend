package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.admin.AdminService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.security.access.prepost.PreAuthorize;
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
