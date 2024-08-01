package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.CartDTO;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.Cart;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "createdAt", ignore = true)
    CartDTO toDTO(Cart cart);

    @Mapping(target = "createdAt", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    CartItemDTO toCartItemDTO(CartItem cartItem);

    CartItem toCartItemEntity(CartItemDTO cartItemDTO);

}


