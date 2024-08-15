package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.OtpService;
import com.ghtk.ecommercewebsite.services.RedisOtpService;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/seller")
@RequiredArgsConstructor
public class SellerController {

    @Autowired
    private final SellerService sellerService;
    private final RedisOtpService redisOtpService;
    private final UserService userService;
    private final OtpService otpService;

    @PostMapping("/signup")
    public CommonResult<User> signup(@RequestBody SellerRegisterDto sellerRegisterDto) throws SellerAlreadyExistedException {
        return CommonResult.success(sellerService.signUpSeller(sellerRegisterDto));
    }

    // New version
    @PostMapping("/signUpNewVersion")
    public CommonResult<User> verifyOtpForSigningUpSeller(@RequestBody SellerRegisterDto sellerRegisterDto) {
        return CommonResult.success(sellerService.signUpNewVersion(sellerRegisterDto));
    }

    // New version here
    @PostMapping("/verifyOtp")
    public CommonResult<String> verifyOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        Integer otp = Integer.parseInt(requestBody.get("otp"));
        boolean isOtpValid = redisOtpService.verifyOtp(email, otp);
        if (!isOtpValid) { return CommonResult.failed("Invalid OTP"); }
        userService.activateUser(email);
        return CommonResult.success("Seller account activated successfully");
    }

    // New version here
    @PostMapping("/resendOtp")
    public CommonResult<String> resendOtp(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        return otpService.resendOtpForSigningUp(email);
    }

    @PostMapping("/login")
    public CommonResult<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) throws AccessDeniedException {
        return CommonResult.success(sellerService.authenticateSellerAndGetLoginResponse(loginUserDto));
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/addShopInfo")
    public CommonResult<Shop> updateShopInfoAfterLogin(@RequestBody ShopDTO shopDTO) throws DataNotFoundException {
        User authenticatedSeller = sellerService.getAuthenticatedSeller();
//        Shop shop = sellerService.updateShopInfo(authenticatedSeller, shopDTO);
        return CommonResult.success(sellerService.updateShopInfo(authenticatedSeller.getId(), shopDTO));
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<User> authenticatedSeller() {
        return CommonResult.success(sellerService.getAuthenticatedSeller());
    }

    @GetMapping("/information")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<DetailSellerInfoDTO> getInformationSeller() throws  Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return  CommonResult.success(sellerService.getDetailSellerInfor(user.getId()),"get information seller successfully");
        return  CommonResult.success(sellerService.getSellerInfo(user.getId()),"get information seller successfully");
    }

    @PutMapping("/information")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult updateInformationSeller(@RequestBody DetailSellerInfoDTO detailSellerInfoDTO) throws  Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(sellerService.updateSellerInfo(detailSellerInfoDTO, user.getId()),"get information seller successfully");
    }
}
