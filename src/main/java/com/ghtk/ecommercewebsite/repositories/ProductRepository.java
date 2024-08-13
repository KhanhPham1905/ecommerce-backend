package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN ProductCategory pc ON p.id = pc.productId WHERE pc.categoryId = :categoryId")
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.brandId = ?1")
    List<Product> findByBrandId(Long brandId);

    List<Product> findByNameContaining(String name);


    List<Product> findByDescriptionContaining(String description);


    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.categoryList c " +
            "WHERE (:categoryIds IS NULL OR c.id IN :categoryIds) " +
            "AND (:brandIds IS NULL OR p.brandId IN :brandIds) " +
            "AND (:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%) " +
            "GROUP BY p.id " +
            "HAVING (:categoryIds IS NULL OR COUNT(DISTINCT c.id) = :categoryCount)")
    Page<Product> searchProducts(
            @Param("categoryIds") List<Long> categoryIds,
            @Param("categoryCount") long categoryCount,
            @Param("brandIds") List<Long> brandIds,
            @Param("keyword") String keyword,
            Pageable pageable);
    }
