package com.ghtk.ecommercewebsite.services.inventory;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.dtos.InventoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InventoryService {
    InventoryDTO getInventoryById(Long Userid) ;

    Page<DetailInventoryDTO> getAllInventory(String warehouse,String skuCode,String name,Long userId, Pageable pageable);

    DetailInventoryDTO importWarehouse(DetailInventoryDTO detailInventoryDTO,Long userId) ;

    Page<DetailInventoryDTO> getListImport(String warehouse,String supplier,String location,String skuCode,String name ,String createdAt,Long userId, Pageable pageable);
    Page<DetailInventoryDTO>  getListExport(String warehouse,String supplier,String location,String skuCode,String name ,String createdAt, Long userId, Pageable pageable);

}
