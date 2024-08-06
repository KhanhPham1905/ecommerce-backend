package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.ProductItemAttributes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemAttributesRepository extends JpaRepository<ProductItemAttributes, Long> {
    List<ProductItemAttributes>  findByProductItemId(Long productItemId);
}
