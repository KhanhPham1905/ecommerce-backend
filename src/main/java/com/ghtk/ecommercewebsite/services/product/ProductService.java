package com.ghtk.ecommercewebsite.services.product;


import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.repositories.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductService(ProductsRepository productsRepository) {
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
}
