package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public Brand toEntity(BrandDTO dto) {
        if (dto == null) {
            return null;
        }

        Brand brand = new Brand();
        brand.setId(dto.getId());
        brand.setDescription(dto.getDescription());
        brand.setName(dto.getName());
        brand.setStatus(dto.isStatus());
        brand.setShopId(dto.getShopId());
        // Ignore createdAt and modifiedAt as per the requirement
        return brand;
    }

    public BrandDTO toDTO(Brand brand) {
        if (brand == null) {
            return null;
        }

        BrandDTO dto = new BrandDTO();
        dto.setId(brand.getId());
        dto.setDescription(brand.getDescription());
        dto.setName(brand.getName());
        dto.setStatus(brand.isStatus());
        dto.setShopId(brand.getShopId());
        // Ignore createdAt and modifiedAt as per the requirement
        return dto;
    }
}
