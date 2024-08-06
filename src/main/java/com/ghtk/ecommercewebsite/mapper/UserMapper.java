package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.SellerDTO;
import com.ghtk.ecommercewebsite.models.dtos.UserDTO;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
