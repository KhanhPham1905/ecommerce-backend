package com.ghtk.ecommercewebsite.services.brand;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {
    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final ProductItemRepository productItemRepository;
    private final ProductAttributesRepository productAttributesRepository;

    @Override
    public Brand getBrandById(Long id) throws Exception{
        return brandRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
    }

    @Override
    @Transactional
    public Brand createBrand(BrandDTO brandDTO, Long userId)  throws Exception {
//        Long shopId = sellerRepository.findShopIdByUserId(userId);
//        if (shopId == null){
//            throw new DataNotFoundException("Cannot find Shop id by Userid");
//        }
        Brand newBrand = Brand
                .builder()
                .isDelete(Boolean.FALSE)
                .name(brandDTO.getName())
                .status(brandDTO.getStatus())
                .build();
        return brandRepository.save(newBrand);
    }

    @Override
    public Page<Brand> getAllBrands(PageRequest pageRequest,  String name) throws Exception{
//        Long shopId = sellerRepository.findShopIdByUserId(userId);
//        if (shopId == null){
//            throw new DataNotFoundException("Cannot find Shop id by Userid");
//        }
        return brandRepository.findAllBrand(name, pageRequest);
    }

    @Override
    @Transactional
    public Brand updateBrand(Long brandId, BrandDTO brandDTO, Long userId) throws  Exception {
//        Long shopId = sellerRepository.findShopIdByUserId(userId);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category by id"));
//        if(!brand.getShopId().equals(shopId)) {
//            throw new AccessDeniedException("account seller and shop not match");
//        }
        Brand existingBrand = getBrandById(brandId);
        existingBrand.setName(brandDTO.getName());
        existingBrand.setStatus(brandDTO.getStatus());
        brandRepository.save(existingBrand);
        return existingBrand;
    }

    @Override
    @Transactional
    public Brand deleteBrand(Long id, Long userId) throws Exception {
//        Long shopId = sellerRepository.findShopIdByUserId(userId);
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category by id"));
//        if(!brand.getShopId().equals(shopId)) {
//            throw new AccessDeniedException("Account seller and shop not match");
//        }
//        List<Product> products = productRepository.findByCategoryId(id);
            brand.setIsDelete(Boolean.TRUE);
            brandRepository.save(brand);
            productRepository.softDeleteProductByCategoryId(id);
            productAttributesRepository.softDeleteProductAttributesByProductId(id);
            productItemRepository.softDeleteProductItemByProductId(id);
        return brand;
    }
}
