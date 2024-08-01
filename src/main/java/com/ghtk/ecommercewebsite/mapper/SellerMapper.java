package com.ghtk.ecommercewebsite.mapper;
import com.ghtk.ecommercewebsite.models.dtos.SellerDTO;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {

//    @Mapping
    SellerDTO toDTO(Seller seller);

//    @Mapping(target = "createdAt", ignore = true)
    Seller toEntity(SellerDTO sellerDTO);
}
