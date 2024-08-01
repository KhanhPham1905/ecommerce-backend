package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.entities.Cart;
import com.ghtk.ecommercewebsite.models.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartService {

    List<Cart> findAll();

    Optional<Cart> findById(Long id);

    Cart save(Cart cart);

    void deleteById(Long id);

    CartItem addProductToCart(Long userId, Long productItemId, int quantity) ;
}
