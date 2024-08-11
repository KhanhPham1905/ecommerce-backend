package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.ProductItemDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import org.springframework.stereotype.Component;

@Component
public class ProductItemMapper {

    // Chuyển đổi từ ProductItem entity sang ProductItemDTO
    public ProductItemDTO toDTO(ProductItem productItem) {
        if (productItem == null) {
            return null;
        }

        ProductItemDTO dto = new ProductItemDTO();
        dto.setId(productItem.getId());
        dto.setPrice(productItem.getPrice());
        dto.setQuantity(productItem.getQuantity());
        dto.setSalePrice(productItem.getSalePrice());
        dto.setProductId(productItem.getProductId());
        dto.setStatus(productItem.getStatus());
        return dto;
    }

    // Chuyển đổi từ ProductItemDTO sang ProductItem entity
    public ProductItem toEntity(ProductItemDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductItem productItem = ProductItem.builder()
                .id(dto.getId())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .salePrice(dto.getSalePrice())
                .productId(dto.getProductId())
                .status(dto.getStatus())

                .build();

        return productItem;
    }
}
