package com.ghtk.ecommercewebsite.services.brand;

import com.ghtk.ecommercewebsite.models.entities.Brand;

import java.util.List;
import java.util.Optional;

public interface IBrandService {

    // Lấy tất cả các brand có trạng thái hoạt động (status = true)
    List<Brand> findAllActive();

    // Tìm brand theo ID và có trạng thái hoạt động (status = true)
    Optional<Brand> findActiveById(Long id);

    // Tạo mới một brand
    Brand createBrand(Brand brand);

    // Cập nhật thông tin của một brand
    Brand updateBrand(Long id, Brand brandDetails);

    // Cập nhật một phần thông tin của một brand
    Brand patchBrand(Long id, Brand brandDetails);

    // Xóa mềm một brand (chỉ thay đổi trạng thái status thành false)
    void softDeleteBrand(Long id);
}
