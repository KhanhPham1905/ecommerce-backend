package com.ghtk.ecommercewebsite.services.productitem;

import com.ghtk.ecommercewebsite.models.dtos.DetailProductItemDTO;
import com.ghtk.ecommercewebsite.models.dtos.ListAttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ProductItemService {
    DetailProductItemDTO createProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws Exception;

    Page<Object> getAllProductItem(Long productId, Long userId, Pageable pageable) throws Exception;

    DetailProductItemDTO updateProductItem(DetailProductItemDTO detailProductItemDTO, Long userId) throws  Exception;

    void deleteProductItem(Long id, Long userId) throws  Exception;
    DetailProductItemDTO getProductItemById(Long id, Long userId) throws  Exception;

//    ProductItem getProductItemByAttributesValues(Long id, ListAttributeValuesDTO listAttributeValuesDTO) throws Exception;
}
