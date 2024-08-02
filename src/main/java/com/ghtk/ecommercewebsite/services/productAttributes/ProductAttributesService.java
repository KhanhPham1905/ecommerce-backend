package com.ghtk.ecommercewebsite.services.productAttributes;

import com.ghtk.ecommercewebsite.models.dtos.ProductAttributesDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;

import java.util.List;

public interface ProductAttributesService {
    ProductAttributesDTO createProductAttributes(ProductAttributesDTO productAttributesDTO,Long id, Long userId) throws Exception;
    ProductAttributesDTO getProductAttributesById(Long id) throws  Exception;
    ProductAttributesDTO updateProductAttributes(ProductAttributesDTO productAttributesDTO, Long user) throws  Exception;
    ProductAttributesDTO deleteProductAttributes(Long id, Long userId) throws Exception;
    List<ProductAttributes> getAllProductAttributes(Long idProduct) throws Exception;
}
