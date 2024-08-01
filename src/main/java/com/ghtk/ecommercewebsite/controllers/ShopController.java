package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.DetailSellerInfoDTO;
import com.ghtk.ecommercewebsite.models.dtos.DetailShopInfoDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.seller.SellerService;
import com.ghtk.ecommercewebsite.services.shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShopController {

    private final ShopService shopService;
    @GetMapping("")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<DetailShopInfoDTO> getInformationShop() throws  Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(shopService.getShopInfo(user.getId()),"get information shop successfully");
    }

    @PutMapping("")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult updateInformationShop(@RequestBody DetailShopInfoDTO detailShopInfoDTO) throws  Exception{
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(shopService.updateShopInfo(detailShopInfoDTO,user.getId()),"get information shop successfully");
    }

}
