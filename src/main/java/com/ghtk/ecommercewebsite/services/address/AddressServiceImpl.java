package com.ghtk.ecommercewebsite.services.address;

import com.ghtk.ecommercewebsite.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public boolean isUserInAddress(Long userId) {
        return addressRepository.existsByUserId(userId);
    }
}

