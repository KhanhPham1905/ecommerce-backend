package com.ghtk.ecommercewebsite.services.shop;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.DetailSellerInfoDTO;
import com.ghtk.ecommercewebsite.models.dtos.DetailShopInfoDTO;
import com.ghtk.ecommercewebsite.models.entities.Address;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.repositories.AddressRepository;
import com.ghtk.ecommercewebsite.repositories.SellerRepository;
import com.ghtk.ecommercewebsite.repositories.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService{
    private  final ShopRepository shopRepository;
    private  final SellerRepository sellerRepository;
    private  final AddressRepository addressRepository;
    @Override
    public DetailShopInfoDTO getShopInfo(Long userId) throws  Exception{
        Long shopId = sellerRepository.findShopIdByUserId(userId);
        if(shopId == null){
            throw new DataNotFoundException("Cannot not found shop id by userId");
        }
        DetailShopInfoDTO detailShopInfoDTO = shopRepository.getDetailShopInfo(shopId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find shop information"));
        return detailShopInfoDTO;
    }

    @Override
    public DetailShopInfoDTO updateShopInfo(DetailShopInfoDTO detailShopInfoDTO ,Long userId) throws Exception {
        Shop shop = shopRepository.findShopByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("shop not found"));
        shop.setMail(detailShopInfoDTO.getMail());
        shop.setName(detailShopInfoDTO.getName());
        shop.setPhone(detailShopInfoDTO.getPhone());

        Address address = addressRepository.findByUserId(userId)
                .orElseThrow(() -> new DataNotFoundException("address not found"));
        address.setAddressDetail(detailShopInfoDTO.getAddressDetail());
        address.setCountry(detailShopInfoDTO.getCountry());
        address.setCommune(detailShopInfoDTO.getCommune());
        address.setProvince(detailShopInfoDTO.getProvince());
        address.setDistrict(detailShopInfoDTO.getDistrict());

        shopRepository.save(shop);
        addressRepository.save(address);
        return detailShopInfoDTO;
    }
}
