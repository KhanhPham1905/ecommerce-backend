package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query(value = "INSERT INTO cart_item (cart_id, product_item_id, quantity) VALUES (:cartId, :productItemId, :quantity)", nativeQuery = true)
    void addProductToCart(@Param("cartId") Long cartId, @Param("productItemId") Long productItemId, @Param("quantity") int quantity);
}
