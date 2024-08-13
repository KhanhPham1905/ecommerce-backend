package com.ghtk.ecommercewebsite.services.product;

import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Product save(ProductDTO productDTO, Long userId) throws Exception;

    void deleteById(Long id);

    List<Product> findByBrandId(Long brandId);
    void deleteBrandById(Long id);
    List<Product> searchProductsByName(String keyword); // Phương thức tìm kiếm

    List<Product> searchProductsByDes(String keyword); // Phương thức tìm kiếm

    List<Product> getAllProducts() throws  Exception;

    Page<ProductResponse> searchProducts (List<Long> categoryIds, long categoryCount, List<Long> brandIds, String keyword, PageRequest pageRequest) throws Exception;
}
