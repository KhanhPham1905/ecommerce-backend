package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Supply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    @Query(name = "Supply.getAllImport", nativeQuery = true)
    List<DetailInventoryDTO> getAllImport(@Param("warehouse") String warehouse, @Param("supplier") String supplier,@Param("location") String location,@Param("sku_code") String skuCode,@Param("name") String name ,@Param("created_at")String createdAt,@Param("shop_id") Long shopId, @Param("limit") int limit,
                                          @Param("offset") int offset);

    @Query(name = "Supply.getAllExport", nativeQuery = true)
    List<DetailInventoryDTO> getAllExport(@Param("warehouse") String warehouse, @Param("supplier") String supplier,@Param("location") String location,@Param("sku_code") String skuCode,@Param("name") String name ,@Param("created_at")String createdAt,@Param("shop_id") Long shopId, @Param("limit") int limit,
                                          @Param("offset") int offset);
}
