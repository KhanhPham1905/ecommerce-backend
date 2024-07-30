package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.mapper.BrandMapper;
import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.services.brand.IBrandService;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import com.ghtk.ecommercewebsite.services.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/brands")
public class BrandController {

    private final BrandMapper brandMapper;
    private final IBrandService iBrandService;

    private  final IProductService iProductService ;

    @Autowired
    public BrandController(BrandMapper brandMapper, IBrandService iBrandService, ProductServiceImpl productService, IProductService iProductService) {
        this.brandMapper = brandMapper;
        this.iBrandService = iBrandService;
        this.iProductService = iProductService;
    }

    @GetMapping
    public List<BrandDTO> getAllBrands() {
        return iBrandService.findAll().stream().map(brandMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDTO> getBrandByID(@PathVariable Long id) {
        return iBrandService.findById(id).map(brand -> ResponseEntity.ok(brandMapper.toDTO(brand)))
                .orElse(ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.toEntity(brandDTO);
        Brand savedBrand = iBrandService.save(brand);
        return ResponseEntity.ok(brandMapper.toDTO(savedBrand));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> updateBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        return iBrandService.findById(id)
                .map(brand -> {
                    brand.setDescription(brandDetails.getDescription());
                    brand.setName(brandDetails.getName());
                    brand.setStatus(brandDetails.isStatus());
                    brand.setShopId(brandDetails.getShopId());
                    Brand updateBrand = iBrandService.save(brand);
                    return ResponseEntity.ok(brandMapper.toDTO(updateBrand));
                }).orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BrandDTO> patchBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        return iBrandService.findById(id)
                .map(brand -> {
                    if (brandDetails.getDescription() != null) brand.setDescription(brandDetails.getDescription());
                    if (brandDetails.getName() != null) brand.setName(brandDetails.getName());
                    if (brandDetails.getShopId() != null) brand.setShopId(brandDetails.getShopId());
                    Brand updatedBrand = iBrandService.save(brand);
                    return ResponseEntity.ok(brandMapper.toDTO(updatedBrand));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long id) {
        return iBrandService.findById(id)
                .map(brand -> {
                    iProductService.deleteBrandById(id);
                    iBrandService.deleteById(id);
                    return ResponseEntity.ok("Brand with ID " + id + " has been deleted.");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
