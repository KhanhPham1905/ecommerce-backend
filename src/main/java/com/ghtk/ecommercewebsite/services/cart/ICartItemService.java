package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface ICartItemService {
    CartItem getCartItemById(Long id) throws Exception;
    void createCartItem(CartItemDTO cartItemDTO, Long userId) throws Exception;
    Page<CartItem> getAllCartItems(PageRequest pageRequest, Long userId) throws Exception;
    void deleteCartItem(Long id, Long userId) throws Exception;
    CartItem updateCartItem(Long id, CartItemDTO cartItemDTO, Long userId) throws Exception;
}