package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.dtos.SellerDTO;
import com.ghtk.ecommercewebsite.models.dtos.UserDTO;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.services.admin.AdminService;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final SellerService sellerService;
    private final AdminService adminService;

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto){
        return CommonResult.success(adminService.authenticateAdminAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> authenticatedAdmin() {
        return CommonResult.success(adminService.getAuthenticatedAdmin());
    }

    @GetMapping("/sellers")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Page<User>> allSellers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return CommonResult.success(userService.allSellers(pageable));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Page<User>> allUsers (
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return CommonResult.success(userService.allUsers(pageable));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> viewUserDetails(@PathVariable Long id){
        return CommonResult.success(userService.viewDetailsOfAnUser(id));
    }

    @GetMapping("/sellers/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> viewSellerDetails(@PathVariable Long id){
        return CommonResult.success(sellerService.viewDetailsOfAnSeller(id));
    }

    @PostMapping("/users/add")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> addNewUser(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return CommonResult.success(userService.signUp(registerUserDto));
    }

    @PostMapping("/users/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<User> updateUserInfo(@Valid @RequestBody UserDTO userDTO){
        return CommonResult.success(userService.updateUserInfo(userDTO));
    }

    @PostMapping("/sellers/update")
    @PreAuthorize("hasRole('ADMIN')")
    public CommonResult<Seller> updateSellerInfo(@Valid @RequestBody SellerDTO sellerDTO){
        return CommonResult.success(sellerService.updateSellerInfo(sellerDTO));
    }
}
