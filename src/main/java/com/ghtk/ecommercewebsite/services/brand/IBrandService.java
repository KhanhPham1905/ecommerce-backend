package com.ghtk.ecommercewebsite.services.brand;


import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.Product;


import java.util.List;
import java.util.Optional;

public interface IBrandService {

    List<Brand> findAll();
    Optional<Brand> findById(Long id);
    Brand save(Brand product);

    void deleteById(Long id);
}
