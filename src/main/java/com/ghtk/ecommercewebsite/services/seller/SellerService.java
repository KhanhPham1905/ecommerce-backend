package com.ghtk.ecommercewebsite.services.seller;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.*;
import com.ghtk.ecommercewebsite.models.entities.Seller;
import com.ghtk.ecommercewebsite.models.entities.Shop;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface SellerService {
    User signUpSeller(SellerRegisterDto input);
    LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto);
    User getAuthenticatedSeller();
    DetailSellerInfoDTO getSellerInfo(Long useId);
    DetailSellerInfoDTO updateSellerInfo(DetailSellerInfoDTO detailSellerInfoDTO,Long useId);

    User viewDetailsOfAnSeller(Long id);

    Seller updateSellerInfo(SellerDTO sellerDTO);

    Shop updateShopInfo(Long userId, ShopDTO shopDTO);

    User signUpNewVersion(SellerRegisterDto sellerRegisterDto);

    User signUpNewestVersion(RegisterUserDto registerUserDto);

    Map<String, Long> getBasicInfo(Long userId);
}
