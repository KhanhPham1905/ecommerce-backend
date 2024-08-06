package com.ghtk.ecommercewebsite.services.inventory;

import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.dtos.InventoryDTO;

import java.util.List;

public interface InventoryService {
    InventoryDTO getInventoryById(Long Userid) throws Exception;
    List<DetailInventoryDTO> getAllInventory(Long userId) throws Exception;

    DetailInventoryDTO importWarehouse(DetailInventoryDTO detailInventoryDTO,Long userId) throws Exception;

    List<DetailInventoryDTO> getListImport(Long userId) throws Exception;
    List<DetailInventoryDTO>  getListExport(Long userId) throws Exception;
}
