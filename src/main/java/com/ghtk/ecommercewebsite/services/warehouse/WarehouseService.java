package com.ghtk.ecommercewebsite.services.warehouse;

import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import com.ghtk.ecommercewebsite.models.dtos.WarehouseDto;
import com.ghtk.ecommercewebsite.models.entities.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface WarehouseService {
    DetailWarehouseDTO getWarehouseInfo(Long id);
    DetailWarehouseDTO createWarehouse(DetailWarehouseDTO detailWarehouseDTO, Long userId);
    DetailWarehouseDTO updateWarehouseById(DetailWarehouseDTO detailWarehouseDTO, Long id, Long userId);
    void deleteWarehouseById(Long id, Long userId);
    Page<Warehouse> getAllWarehouse(PageRequest pageRequest, Long userId, String name);
}
