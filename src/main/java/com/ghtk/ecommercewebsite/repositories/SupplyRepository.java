package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<Supply, Long> {

    @Query(name = "Supply.getAllImport", nativeQuery = true)
    List<DetailInventoryDTO> getAllImport(@Param("shop_id") Long shopId);

    @Query(name = "Supply.getAllExport", nativeQuery = true)
    List<DetailInventoryDTO> getAllExport(@Param("shop_id") Long shopId);
}
