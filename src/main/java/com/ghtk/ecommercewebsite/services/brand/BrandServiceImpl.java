package com.ghtk.ecommercewebsite.services.brand;

import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.repositories.BrandRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements IBrandService {

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    @Override
    public List<Brand> findAllActive() {
        return brandRepository.findAll().stream()
                .filter(Brand::isStatus)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Brand> findActiveById(Long id) {
        return brandRepository.findById(id)
                .filter(Brand::isStatus);
    }

    @Override
    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    public Brand updateBrand(Long id, Brand brandDetails) {
        return brandRepository.findById(id)
                .filter(Brand::isStatus)
                .map(brand -> {
                    brand.setDescription(brandDetails.getDescription());
                    brand.setName(brandDetails.getName());
                    brand.setStatus(brandDetails.isStatus());
                    brand.setShopId(brandDetails.getShopId());
                    return brandRepository.save(brand);
                }).orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Override
    public Brand patchBrand(Long id, Brand brandDetails) {
        return brandRepository.findById(id)
                .filter(Brand::isStatus)
                .map(brand -> {
                    if (brandDetails.getDescription() != null) brand.setDescription(brandDetails.getDescription());
                    if (brandDetails.getName() != null) brand.setName(brandDetails.getName());
                    if (brandDetails.getShopId() != null) brand.setShopId(brandDetails.getShopId());
                    return brandRepository.save(brand);
                }).orElseThrow(() -> new RuntimeException("Brand not found"));
    }

    @Override
    public void softDeleteBrand(Long id) {
        brandRepository.findById(id).ifPresent(brand -> {
            // Soft delete brand
            brand.setStatus(false);
            brandRepository.save(brand);

            // Soft delete all products associated with this brand
            List<Product> products = productRepository.findByBrandId(id);
            products.forEach(product -> {
                product.setStatus(false);
                productRepository.save(product);
            });
        });
    }
}
