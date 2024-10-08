package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import com.ghtk.ecommercewebsite.models.dtos.WarehouseDto;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.entities.Warehouse;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query(name = "Warehouse.getDetailWarehouseInfo", nativeQuery = true)
    DetailWarehouseDTO findDetailByWarehouseId(@Param("warehouseId") Long warehouseId);

    @Query("SELECT w.shopId FROM Warehouse w WHERE w.id = :id")
    Long findShopIdById(@Param("id") Long id);

    @Query(value = "SELECT * FROM warehouse w " +
            "WHERE w.shop_id = ?1 " +
            "AND w.name LIKE CONCAT('%',?2,'%') " +
            "AND w.is_delete = 0", nativeQuery = true)
    Page<Warehouse> findByShopId(Long id, String name, Pageable pageable) ;


    @Query("SELECT COUNT(*) FROM Warehouse w WHERE w.shopId = ?1")
    Long getQuantityByShopId(Long shopId);
}
