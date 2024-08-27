package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserId(Long userId); // Thêm phương thức này

    List<Orders> findAllByUserId(Long userId);

    List<Orders> findAllByShopId(Long ShopId);

    @Query("SELECT COUNT(*) FROM Orders v WHERE v.shopId = ?1")
    Long getQuantityByShopId(Long shopId);


    @Query("SELECT o FROM Orders o WHERE o.shopId = ?1")
    List<Orders> findAll(Long shopId);
//    @Query("SELECT COUNT(*) FROM Orders o WHERE o.shopId = ?1")
//    Long getQuantityByShopId(Long shopId);
}
