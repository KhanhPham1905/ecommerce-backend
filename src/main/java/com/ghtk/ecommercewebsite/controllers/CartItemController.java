package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.CartItemMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.cart.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    private final ICartItemService cartItemService;

    private final CartItemMapper cartItemMapper ;

    @Autowired
    public CartItemController(ICartItemService cartItemService, CartItemMapper cartItemMapper) {
        this.cartItemService = cartItemService;
        this.cartItemMapper = cartItemMapper;
    }

    @PostMapping("/create_cart_item")
    public CommonResult<CartItemDTO> createCartItem(@RequestBody CartItemDTO cartItemDTO) {
        CartItem savedCartItem = cartItemService.save(cartItemMapper.toEntity(cartItemDTO));
        return CommonResult.success(cartItemMapper.toDTO(savedCartItem), "Create cart item successfully");
    }
    @PostMapping("/add")
    public CommonResult<Object> addProductToCart(@RequestBody CartItemDTO cartItemDTO) {
        try {
            cartItemService.addProductToCart(cartItemDTO);
            return CommonResult.success(null, "Product added to cart successfully");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public CommonResult<Object> deleteCartItems(@RequestBody CartItemDTO cartItemDTO) {
        try {
            cartItemService.deleteCartItems(cartItemDTO.getUserId(), cartItemDTO.getProductItemId());
            return CommonResult.success(null, "Cart items deleted successfully");
        } catch (IllegalArgumentException e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
