package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
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

    @PostMapping("/signup")
    public CommonResult<User> signup(@RequestBody SellerRegisterDto sellerRegisterDto) throws SellerAlreadyExistedException {
        return CommonResult.success(sellerService.signUpSeller(sellerRegisterDto));
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
