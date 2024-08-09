package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Transactional
    void deleteByProductItemId(Long productItemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.userId = :userId AND ci.productItemId = :productItemId")
    void deleteByUserIdAndProductId(Long userId, Long productItemId);

    List<CartItem> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}
