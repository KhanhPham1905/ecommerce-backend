package com.ghtk.ecommercewebsite.services.productAttributes;

import com.ghtk.ecommercewebsite.models.dtos.ProductAttributesDTO;
import com.ghtk.ecommercewebsite.models.entities.ProductAttributes;

import java.util.List;

public interface ProductAttributesService {
    ProductAttributesDTO createProductAttributes(ProductAttributesDTO productAttributesDTO,Long id, Long userId);
    ProductAttributesDTO getProductAttributesById(Long id);
    ProductAttributesDTO updateProductAttributes(ProductAttributesDTO productAttributesDTO, Long user);
    ProductAttributesDTO deleteProductAttributes(Long id, Long userId);
    List<ProductAttributes> getAllProductAttributes(Long idProduct);
}
