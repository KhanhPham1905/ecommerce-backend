package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.cart.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final CartItemMapper cartItemMapper;



    @PostMapping("/add")
    public CommonResult<Object> addProductToCart(@RequestBody CartItemDTO cartItemDTO) {
        try {
            cartItemService.addProductToCart(cartItemDTO);
            return CommonResult.success(null, "Product added to cart successfully");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @PutMapping("/update")
    public CommonResult<Object> updateCartItem(@RequestBody CartItemDTO cartItemDTO) {
        try {
            cartItemService.updateCartItem(cartItemDTO);
            return CommonResult.success(null, "Cart item updated successfully");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
