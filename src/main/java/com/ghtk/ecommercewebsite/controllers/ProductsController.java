package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final IProductService iProductService;
    private final ProductMapper productMapper;

    @GetMapping
    public CommonResult<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = iProductService.findAllActive().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Get all products successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<ProductDTO> getProductById(@PathVariable Long id) {
        return iProductService.findActiveById(id)
                .map(product -> CommonResult.success(productMapper.toDTO(product), "Get product successfully"))
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @PostMapping
    public CommonResult<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = iProductService.createProduct(product);
        return CommonResult.success(productMapper.toDTO(savedProduct), "Create product successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDetails) {
        Product updatedProduct = iProductService.updateProduct(id, productMapper.toEntity(productDetails));
        return CommonResult.success(productMapper.toDTO(updatedProduct), "Update product successfully");
    }

    @PatchMapping("/{id}")
    public CommonResult<ProductDTO> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        Product updatedProduct = iProductService.patchProduct(id, updates);
        return CommonResult.success(productMapper.toDTO(updatedProduct), "Patch product successfully");
    }

    @DeleteMapping("/{id}")
    public CommonResult<String> deleteProduct(@PathVariable Long id) {
        iProductService.softDeleteProduct(id);
        return CommonResult.success("Product with ID " + id + " has been soft deleted.");
    }

    @GetMapping("/searchName")
    public CommonResult<List<ProductDTO>> searchProductsByName(@RequestParam("keyword") String keyword) {
        List<ProductDTO> products = iProductService.searchProductsByName(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return products.isEmpty() ? CommonResult.error(404, "not found")
                : CommonResult.success(products, "Search products by name successfully");
    }

    @GetMapping("/searchDes")
    public CommonResult<List<ProductDTO>> searchProductsByDes(@RequestParam("keyword") String keyword) {
        List<ProductDTO> products = iProductService.searchProductsByDes(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Search products by description successfully");
    }
}
