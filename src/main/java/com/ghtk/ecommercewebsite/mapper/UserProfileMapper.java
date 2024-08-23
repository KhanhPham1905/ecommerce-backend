package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.UserProfileDTO;
import com.ghtk.ecommercewebsite.models.entities.Address;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class UserProfileMapper {

    public static final UserProfileMapper INSTANCE = Mappers.getMapper(UserProfileMapper.class);

    @Autowired
    protected AddressRepository addressRepository;

    public abstract UserProfileDTO toDto(User user, Address address);

    public UserProfileDTO toDTO(User user) throws DataNotFoundException {
        Address address = addressRepository.findById(user.getAddressId())
                .orElse(null);
        if (address != null) {
            return toDto(user, address);
        } else {
            throw new DataNotFoundException("Address not found");
        }
    }
}
