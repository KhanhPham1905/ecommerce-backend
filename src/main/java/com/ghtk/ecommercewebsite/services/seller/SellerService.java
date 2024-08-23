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
    User signUpSeller(SellerRegisterDto input) throws SellerAlreadyExistedException;
    LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    User getAuthenticatedSeller();
    DetailSellerInfoDTO getSellerInfo(Long useId) throws  Exception;
    DetailSellerInfoDTO updateSellerInfo(DetailSellerInfoDTO detailSellerInfoDTO,Long useId) throws  Exception;

    User viewDetailsOfAnSeller(Long id) throws DataNotFoundException;

    Seller updateSellerInfo(SellerDTO sellerDTO) throws DataNotFoundException;

    Shop updateShopInfo(Long userId, ShopDTO shopDTO) throws DataNotFoundException;

    User signUpNewVersion(SellerRegisterDto sellerRegisterDto);

    User signUpNewestVersion(RegisterUserDto registerUserDto) throws DataNotFoundException;

    Map<String, Long> getBasicInfo(Long userId) throws DataNotFoundException;
}
