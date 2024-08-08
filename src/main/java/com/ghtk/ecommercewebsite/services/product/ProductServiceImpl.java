package com.ghtk.ecommercewebsite.services.product;


import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements IProductService {
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productsRepository;

    public ProductServiceImpl(CloudinaryService cloudinaryService, ProductRepository productsRepository) {
        this.cloudinaryService = cloudinaryService;
        this.productsRepository = productsRepository;
    }

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productsRepository.findById(id);
    }

    public Product save(Product product) {
        return productsRepository.save(product);
    }

    public void deleteById(Long id) {
        productsRepository.deleteById(id);
    }

    @Override
    public List<Product> findByBrandId(Long brandId) {
        return productsRepository.findByBrandId(brandId);
    }

    @Override
    public void deleteBrandById(Long brandId) {
        List<Product> products = findByBrandId(brandId);
        for (Product product : products) {
            deleteById(product.getId());
        }
    }

    @Override
    public List<Product> searchProductsByName(String keyword) {
        return productsRepository.findByNameContaining(keyword);
    }

    @Override
    public List<Product> searchProductsByDes(String keyword) {
        return productsRepository.findByDescriptionContaining(keyword);
    }


}
