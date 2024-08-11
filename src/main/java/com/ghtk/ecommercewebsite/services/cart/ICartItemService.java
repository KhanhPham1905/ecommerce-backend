package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {

    void addProductToCart(CartItemDTO cartItemDTO) ;

    void softDeleteCartItem(Long cartItemId) ;

    void updateCartItem(CartItemDTO cartItemDTO);


}
