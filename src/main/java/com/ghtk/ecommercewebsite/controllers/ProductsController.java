package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import com.ghtk.ecommercewebsite.services.product.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final IProductService iProductService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductsController(IProductService productService, ProductMapper productMapper) {
        this.iProductService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return iProductService.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return iProductService.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDTO(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = iProductService.save(product);
        return ResponseEntity.ok(productMapper.toDTO(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDetails) {
        return iProductService.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setSlug(productDetails.getSlug());
                    product.setStatus(productDetails.getStatus());
                    product.setTotalSold(productDetails.getTotalSold());
                    product.setProductView(productDetails.getProductView());
                    product.setBrandId(productDetails.getBrandId());
                    product.setShopId(productDetails.getShopId());
                    Product updatedProduct = iProductService.save(product);
                    return ResponseEntity.ok(productMapper.toDTO(updatedProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return iProductService.findById(id)
                .map(product -> {
                    updates.forEach((key, value) -> {
                        switch (key) {
                            case "name":
                                product.setName((String) value);
                                break;
                            case "description":
                                product.setDescription((String) value);
                                break;
                            case "slug":
                                product.setSlug((String) value);
                                break;
                            case "status":
                                product.setStatus((Integer) value);
                                break;
                            case "totalSold":
                                product.setTotalSold(((Number) value).longValue());
                                break;
                            case "productView":
                                product.setProductView((Integer) value);
                                break;
                            case "brandId":
                                product.setBrandId(((Number) value).longValue());
                                break;
                            case "shopId":
                                product.setShopId(((Number) value).longValue());
                                break;
                        }
                    });
                    Product updatedProduct = iProductService.save(product);
                    return ResponseEntity.ok(productMapper.toDTO(updatedProduct));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
        return iProductService.findById(id)
                .map(product -> {
                    iProductService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/searchName")
    public List<ProductDTO> searchProductsByName(@RequestParam("keyword") String keyword) {
        return iProductService.searchProductsByName(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());

    }
    @GetMapping("/searchDes")
    public List<ProductDTO> searchProductsByDes(@RequestParam("keyword") String keyword) {
        return iProductService.searchProductsByDes(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());

    }

}
