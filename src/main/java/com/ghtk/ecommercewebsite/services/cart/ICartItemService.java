package com.ghtk.ecommercewebsite.services.cart;

import com.ghtk.ecommercewebsite.models.entities.Cart;
import com.ghtk.ecommercewebsite.models.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ICartItemService {


    List<CartItem> findAll();

    Optional<CartItem> findById(Long id);

    CartItem save(CartItem cartItem);

    void deleteById(Long id);
}
