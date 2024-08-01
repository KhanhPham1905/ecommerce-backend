package com.ghtk.ecommercewebsite.services.seller;

import com.ghtk.ecommercewebsite.exceptions.SellerAlreadyExistedException;
import com.ghtk.ecommercewebsite.models.dtos.DetailSellerInfoDTO;
import com.ghtk.ecommercewebsite.models.dtos.LoginUserDto;
import com.ghtk.ecommercewebsite.models.dtos.RegisterUserDto;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.LoginResponse;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public interface SellerService {
    User signUpSeller(RegisterUserDto input) throws SellerAlreadyExistedException;
    LoginResponse authenticateSellerAndGetLoginResponse(LoginUserDto loginUserDto) throws AccessDeniedException;
    User getAuthenticatedSeller();
    DetailSellerInfoDTO getSellerInfo(Long useId) throws  Exception;
    DetailSellerInfoDTO updateSellerInfo(DetailSellerInfoDTO detailSellerInfoDTO,Long useId) throws  Exception;
}
