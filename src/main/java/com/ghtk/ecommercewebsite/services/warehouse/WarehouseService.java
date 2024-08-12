package com.ghtk.ecommercewebsite.services.warehouse;

import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import com.ghtk.ecommercewebsite.models.dtos.WarehouseDto;
import com.ghtk.ecommercewebsite.models.entities.Warehouse;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface WarehouseService {
    DetailWarehouseDTO getWarehouseInfo(Long id) throws Exception;
    DetailWarehouseDTO createWarehouse(DetailWarehouseDTO detailWarehouseDTO, Long userId) throws Exception;
    DetailWarehouseDTO updateWarehouseById(DetailWarehouseDTO detailWarehouseDTO, Long id, Long userId) throws Exception;
    void deleteWarehouseById(Long id, Long userId) throws Exception;
    List<Warehouse> getAllWarehouse(PageRequest pageRequest, Long userId, String name) throws Exception;
}
