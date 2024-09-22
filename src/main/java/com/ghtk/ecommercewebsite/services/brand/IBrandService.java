package com.ghtk.ecommercewebsite.services.brand;


import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.Optional;

public interface IBrandService {
    Brand getBrandById(Long id) ;
    Brand createBrand(BrandDTO brandDTO, Long userId);
    public Page<Brand> getAllBrands(PageRequest pageRequest, String name) ;
    Brand updateBrand(Long categoryId, BrandDTO brandDTO, Long id) ;
    Brand deleteBrand(Long id, Long userId);
}
