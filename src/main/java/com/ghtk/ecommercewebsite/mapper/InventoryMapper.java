package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.InventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Inventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    InventoryDTO toDTO(Inventory inventory);

    Inventory toEntity(InventoryDTO inventoryDTO);
}
