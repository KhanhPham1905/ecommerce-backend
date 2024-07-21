package com.ghtk.ecommercewebsite.controllers;

import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import com.ghtk.ecommercewebsite.services.SellerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody RegisterUserDto registerUserDto) throws SellerAlreadyExistedException {
        return ResponseEntity.ok(sellerService.signUpSeller(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return ResponseEntity.ok(sellerService.authenticateSellerAndGetLoginResponse(loginUserDto));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public ResponseEntity<User> authenticatedSeller() {
        return ResponseEntity.ok(sellerService.getAuthenticatedSeller());
    }
}
