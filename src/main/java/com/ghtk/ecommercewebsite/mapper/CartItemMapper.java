package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItem toEntity(CartItemDTO dto) {
        return CartItem.builder()
                .id(dto.getId())
                .productItemId(dto.getProductItemId())
                .quantity(dto.getQuantity())
                .userId(dto.getUserId())
                .voucherId(dto.getVoucherId())
                .shopId(dto.getShopId())
                .totalPrice(dto.getTotalPrice())
                .build();
    }

    public CartItemDTO toDTO(CartItem entity) {
        return CartItemDTO.builder()
                .id(entity.getId())
                .productItemId(entity.getProductItemId())
                .quantity(entity.getQuantity())
                .userId(entity.getUserId())
                .voucherId(entity.getVoucherId())
                .shopId(entity.getShopId())
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}
