package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByShopId(Long id);

}
