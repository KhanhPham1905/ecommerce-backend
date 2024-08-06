package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.ProductAttributesDTO;
import com.ghtk.ecommercewebsite.models.dtos.SellerDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductAttributesMapper {
    ProductAttributesDTO toDTO(ProductAttributes productAttributes);
    ProductAttributes toEntity(ProductAttributesDTO productAttributesDTO);
}
