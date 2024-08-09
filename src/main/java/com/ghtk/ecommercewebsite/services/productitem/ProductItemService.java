package com.ghtk.ecommercewebsite.services.productitem;

import com.ghtk.ecommercewebsite.models.dtos.DetailProductItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductItemService {
    DetailProductItemDTO createProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws Exception;

    Object getAllProductItem(Long productId, Long userId) throws Exception;

    DetailProductItemDTO updateProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws  Exception;

    void deleteProductItem(Long id, Long userId) throws  Exception;
    DetailProductItemDTO getProductItemById(Long id, Long userId) throws  Exception;
}
