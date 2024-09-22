package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;


public interface ICartItemService {
    CartItem getCartItemById(Long id);
    void createCartItem(CartItemDTO cartItemDTO, Long userId);
    Page<CartItem> getAllCartItems(PageRequest pageRequest, Long userId);
    void deleteCartItem(Long id, Long userId);
    CartItem updateCartItemQuantity(Long cartItemId, int quantity, Long userId);
    Long getQuantityCartItem(Long userId);
    void applyVoucherToCartItem(Long cartItemId, Long voucherId, Long userId);

}