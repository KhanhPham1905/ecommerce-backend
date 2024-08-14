package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    @Query(value = "SELECT * FROM brand b " +
            "WHERE b.shop_id = ?1 " +
            "AND b.name LIKE CONCAT('%',?2,'%') " +
            "AND b.is_delete = 0", nativeQuery = true)
    Page<Brand> findByShopId(Long id, String name, Pageable pageable);
}
