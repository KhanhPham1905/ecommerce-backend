package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

//    @Query(name = "Inventory.getAllInventory", nativeQuery = true)
//    List<DetailInventoryDTO> getAllInventory(@Param("warehouse") String warehouse,@Param("sku_code") String skuCode,@Param("name") String name,@Param("shop_id")Long shopId);
    @Query(name = "Inventory.getAllInventory", nativeQuery = true)
    Page<DetailInventoryDTO> getAllInventory(@Param("warehouse") String warehouse, @Param("sku_code") String skuCode, @Param("name") String name, @Param("shop_id")Long shopId, Pageable pageable);


    @Query("SELECT i FROM Inventory i WHERE i.productItemId = :product_item_id AND i.warehouseId = :warehouse_id")
    Inventory findByProductItemIdAndWarehouseId(@Param("product_item_id") Long productItemId, @Param("warehouse_id") Long warehouseId);
}
