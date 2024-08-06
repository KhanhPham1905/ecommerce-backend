package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {
    CartItem save(CartItem cartItem) ;
    void addProductToCart(CartItemDTO cartItemDTO) ;
    void deleteCartItems(Long userId, Long productItemId);

}
