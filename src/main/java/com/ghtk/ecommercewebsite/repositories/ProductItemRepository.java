package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    @Query(value = "SELECT p.shop_id FROM product_item pi JOIN product p ON pi.product_id = p.id WHERE pi.id = :productItemId", nativeQuery = true)
    Long findShopIdByProductItemId(@Param("productItemId") Long productItemId);
}
