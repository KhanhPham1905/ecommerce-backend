package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagesRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i.link FROM Image i WHERE i.productId = ?1")
    List<String> findLinkByProductId(Long id);

    @Modifying
    @Query(name ="DELETE FROM images i WHERE i.product_id = ?1", nativeQuery = true)
    void deleteByProductId(Long id);
}
