package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.Users;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/signup")
    public CommonResult<Users> signup(@RequestBody RegisterUserDto registerUserDto) throws SellerAlreadyExistedException {
        return CommonResult.success(sellerService.signUpSeller(registerUserDto));
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return CommonResult.success(sellerService.authenticateSellerAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<Users> authenticatedSeller() {
        return CommonResult.success(sellerService.getAuthenticatedSeller());
    }
}
