package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public CommonResult<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = iProductService.findAll().stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Get all products successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<ProductDTO> getProductById(@PathVariable Long id) {
        return iProductService.findById(id)
                .map(product -> CommonResult.success(productMapper.toDTO(product), "Get product successfully"))
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @PostMapping
    public CommonResult<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = productMapper.toEntity(productDTO);
        Product savedProduct = iProductService.save(product);
        return CommonResult.success(productMapper.toDTO(savedProduct), "Create product successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDetails) {
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
                    return CommonResult.success(productMapper.toDTO(updatedProduct), "Update product successfully");
                })
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @PatchMapping("/{id}")
    public CommonResult<ProductDTO> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
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
                    return CommonResult.success(productMapper.toDTO(updatedProduct), "Patch product successfully");
                })
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @DeleteMapping("/{id}")
    public CommonResult<String> deleteProduct(@PathVariable Long id) {
        return iProductService.findById(id)
                .map(product -> {
                    iProductService.deleteById(id);
                    return CommonResult.success("Product with ID " + id + " has been deleted.");
                })
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @GetMapping("/searchName")
    public CommonResult<List<ProductDTO>> searchProductsByName(@RequestParam("keyword") String keyword) {
        List<ProductDTO> products = iProductService.searchProductsByName(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Search products by name successfully");
    }

    @GetMapping("/searchDes")
    public CommonResult<List<ProductDTO>> searchProductsByDes(@RequestParam("keyword") String keyword) {
        List<ProductDTO> products = iProductService.searchProductsByDes(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Search products by description successfully");
    }
}
