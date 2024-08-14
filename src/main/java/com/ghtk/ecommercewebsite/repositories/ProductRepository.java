package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
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

    List<Product> findAllByShopId(Long id);
}
