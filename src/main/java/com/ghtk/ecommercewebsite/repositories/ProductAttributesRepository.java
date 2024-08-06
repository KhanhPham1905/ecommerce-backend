package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductAttributesRepository extends JpaRepository<ProductAttributes, Long> {
    List<ProductAttributes> findByProductId(Long id);
}
