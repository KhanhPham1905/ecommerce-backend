package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.exceptions.UserAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.OtpService;
import com.ghtk.ecommercewebsite.services.RedisOtpService;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final RedisOtpService redisOtpService;
    private final UserService userService;
    private final OtpService otpService;

    @PostMapping("/signUpNewVersion")
    public CommonResult<User> signUpNewestVersion(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return CommonResult.success(sellerService.signUpNewestVersion(registerUserDto));
    }


    // New version here
    @PostMapping("/verifyOtp")
    public CommonResult<String> verifyOtpSellerNewestVersion(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        Integer otp = Integer.parseInt(requestBody.get("otp"));
        boolean isOtpValid = redisOtpService.verifyOtp(email, otp);
        if (!isOtpValid) { return CommonResult.failed("Invalid OTP"); }
        userService.activateUser(email);
        return CommonResult.success("Seller account activated successfully");
    }

    // New version here
    @PostMapping("/resendOtp")
    public CommonResult<String> resendOtpNewestVersion(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return otpService.resendOtpForSigningUp(email);
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@Valid @RequestBody LoginUserDto loginUserDto){
        return CommonResult.success(sellerService.authenticateSellerAndGetLoginResponse(loginUserDto));
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/addShopInfo")
    public CommonResult<Shop> updateShopInfoAfterLogin(@Valid @RequestBody ShopDTO shopDTO){
        User authenticatedSeller = sellerService.getAuthenticatedSeller();
        return CommonResult.success(sellerService.updateShopInfo(authenticatedSeller.getId(), shopDTO));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<User> authenticatedSeller() {
        return CommonResult.success(sellerService.getAuthenticatedSeller());
    }

    @GetMapping("/information")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<DetailSellerInfoDTO> getInformationSeller() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(sellerService.getSellerInfo(user.getId()),"get information seller successfully");
    }

    @PutMapping("/information")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult updateInformationSeller(@Valid @RequestBody DetailSellerInfoDTO detailSellerInfoDTO){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(sellerService.updateSellerInfo(detailSellerInfoDTO, user.getId()),"get information seller successfully");
    }

    @GetMapping("/basicInfor")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public  CommonResult<Map<String, Long>> getBasicInfor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Long> result = new HashMap<>();
        result = sellerService.getBasicInfo(user.getId());
        return CommonResult.success(result,"get basic information success");
    }
}
