package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.cart.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/cart-items")
public class CartItemController {

    private final ICartItemService cartItemService;

    @GetMapping("/{id}")
    public CommonResult<CartItem> getCartItemById(
            @PathVariable("id") Long cartItemId
    ) throws Exception {
        CartItem existingCartItem = cartItemService.getCartItemById(cartItemId);
        return CommonResult.success(existingCartItem, "Get cart item successfully");
    }

    @PostMapping("")
    public CommonResult<Object> createCartItem (
            @Valid @RequestBody CartItemDTO cartItemDTO
    ) throws Exception {
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartItem cartItem = cartItemService.createCartItem(cartItemDTO, user.getId());
        return CommonResult.success(cartItem, "Create cart item successfully");
    }

    @GetMapping("")
    public CommonResult<Page<CartItem>> getAllCartItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) throws Exception {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<CartItem> cartItemPages = cartItemService.getAllCartItems(pageRequest, user.getId());
        return CommonResult.success(cartItemPages, "Get all cart items successfully");
    }

    @DeleteMapping("/{id}")
    public CommonResult<Object> deleteCartItem(@PathVariable Long id) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartItemService.deleteCartItem(id, user.getId());
        return CommonResult.success("Delete cart item successfully");
    }
    @PutMapping("/{id}")
    public CommonResult<CartItem> updateCartItem(
            @PathVariable Long id,
            @Valid @RequestBody CartItemDTO cartItemDTO
    ) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        CartItem cartItem = cartItemService.updateCartItem(id, cartItemDTO, user.getId());
        return CommonResult.success(cartItem, "Update cart item successfully");
    }
}
