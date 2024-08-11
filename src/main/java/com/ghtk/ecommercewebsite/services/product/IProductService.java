package com.ghtk.ecommercewebsite.services.product;

import com.ghtk.ecommercewebsite.models.entities.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductService {

    // Lấy tất cả sản phẩm có trạng thái hoạt động (status = true)
    List<Product> findAllActive();

    // Tìm sản phẩm theo ID và có trạng thái hoạt động (status = true)
    Optional<Product> findActiveById(Long id);

    // Tạo mới một sản phẩm
    Product createProduct(Product product);

    // Cập nhật sản phẩm theo ID
    Product updateProduct(Long id, Product productDetails);

    // Cập nhật từng phần thông tin của sản phẩm theo ID
    Product patchProduct(Long id, Map<String, Object> updates);

    // Xóa mềm sản phẩm theo ID (chỉ thay đổi trạng thái status thành false)
    void softDeleteProduct(Long id);

    // Tìm kiếm sản phẩm theo tên và có trạng thái hoạt động
    List<Product> searchProductsByName(String keyword);

    // Tìm kiếm sản phẩm theo mô tả và có trạng thái hoạt động
    List<Product> searchProductsByDes(String keyword);
}
