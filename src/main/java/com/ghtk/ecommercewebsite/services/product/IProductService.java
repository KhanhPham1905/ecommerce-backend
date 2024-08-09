package com.ghtk.ecommercewebsite.services.product;

import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(ProductDTO productDTO) throws Exception;

    void deleteById(Long id);

    List<Product> findByBrandId(Long brandId);
    void deleteBrandById(Long id);
    List<Product> searchProductsByName(String keyword); // Phương thức tìm kiếm

    List<Product> searchProductsByDes(String keyword); // Phương thức tìm kiếm
}
