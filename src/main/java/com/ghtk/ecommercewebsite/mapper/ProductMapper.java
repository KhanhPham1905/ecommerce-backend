package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setProductView(dto.getProductView());
        product.setTotalSold(dto.getTotalSold());
        product.setSlug(dto.getSlug());
        product.setBrandId(dto.getBrandId());
        product.setShopId(dto.getShopId());
        product.setStatus(dto.getStatus());
        // Ignore createdAt and modifiedAt as per the requirement
        return product;
    }

    public ProductDTO toDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setProductView(product.getProductView());
        dto.setTotalSold(product.getTotalSold());
        dto.setSlug(product.getSlug());
        dto.setBrandId(product.getBrandId());
        dto.setShopId(product.getShopId());
        dto.setStatus(product.getStatus());
        // Ignore createdAt and modifiedAt as per the requirement
        return dto;
    }
}
