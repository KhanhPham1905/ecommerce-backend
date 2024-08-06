package com.ghtk.ecommercewebsite.services.shop;

import com.ghtk.ecommercewebsite.models.dtos.DetailShopInfoDTO;

import java.util.Optional;

public interface ShopService
{
    DetailShopInfoDTO getShopInfo(Long userId) throws Exception;
    DetailShopInfoDTO updateShopInfo(DetailShopInfoDTO detailShopInfoDTO, Long userId) throws Exception;
}
