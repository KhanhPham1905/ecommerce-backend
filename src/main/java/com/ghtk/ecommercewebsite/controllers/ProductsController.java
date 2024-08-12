package com.ghtk.ecommercewebsite.controllers;

import com.cloudinary.Cloudinary;
import com.ghtk.ecommercewebsite.configs.Contant;
import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.models.responses.ProductResponse;
import com.ghtk.ecommercewebsite.services.CloudinaryService;
import com.ghtk.ecommercewebsite.services.images.ImagesService;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final IProductService iProductService;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;

    @GetMapping
    public CommonResult<List<ProductResponse>> getAllProducts() throws Exception{
        List<ProductResponse> products = iProductService.getAllProducts();
        return CommonResult.success(products, "Get all products successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<ProductDTO> getProductById(@PathVariable Long id) {
        return iProductService.findById(id)
                .map(product -> CommonResult.success(productMapper.toDTO(product), "Get product successfully"))
                .orElse(CommonResult.error(404, "Product not found"));
    }

    @PostMapping
    public CommonResult<ProductDTO> createProduct(@ModelAttribute ProductDTO productDTO) throws Exception{
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product savedProduct = iProductService.save(productDTO, user.getId());
        return CommonResult.success(productMapper.toDTO(savedProduct), "Create product successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<ProductDTO> updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDetails){
        return iProductService.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setSlug(productDetails.getSlug());
                    product.setStatus(productDetails.getStatus());
                    product.setTotalSold(productDetails.getTotalSold());
                    product.setProductView(productDetails.getProductView());
                    product.setBrandId(productDetails.getBrandId());
                    Product updatedProduct = null;
                    try {
                        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        updatedProduct = iProductService.save(productDetails, user.getId());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return CommonResult.success(productMapper.toDTO(updatedProduct), "Update product successfully");
                })
                .orElse(CommonResult.error(404, "Product not found"));
    }
//
//    @PatchMapping("/{id}")
//    public CommonResult<ProductDTO> patchProduct(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
//        return iProductService.findById(id)
//                .map(product -> {
//                    updates.forEach((key, value) -> {
//                        switch (key) {
//                            case "name":
//                                product.setName((String) value);
//                                break;
//                            case "description":
//                                product.setDescription((String) value);
//                                break;
//                            case "slug":
//                                product.setSlug((String) value);
//                                break;
//                            case "status":
//                                product.setStatus((Integer) value);
//                                break;
//                            case "totalSold":
//                                product.setTotalSold(((Number) value).longValue());
//                                break;
//                            case "productView":
//                                product.setProductView((Integer) value);
//                                break;
//                            case "brandId":
//                                product.setBrandId(((Number) value).longValue());
//                                break;
//                            case "shopId":
//                                product.setShopId(((Number) value).longValue());
//                                break;
//                        }
//                    });
//                    Product updatedProduct = iProductService.save(product);
//                    return CommonResult.success(productMapper.toDTO(updatedProduct), "Patch product successfully");
//                })
//                .orElse(CommonResult.error(404, "Product not found"));
//    }

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
        if (products.isEmpty()) return CommonResult.error( 404 , "notfounbd");
        else return CommonResult.success(products, "Search products by name successfully");
    }

    @GetMapping("/searchDes")
    public CommonResult<List<ProductDTO>> searchProductsByDes(@RequestParam("keyword") String keyword) {
        List<ProductDTO> products = iProductService.searchProductsByDes(keyword).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
        return CommonResult.success(products, "Search products by description successfully");
    }

    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult<?> uploadImages(
            @PathVariable("id") Long id,
            @ModelAttribute("files") List<MultipartFile> files
    ) throws  Exception{
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if(files.size() > Contant.MAXIMUM_IMAGES_PER_PRODUCT){
            return  CommonResult.failed("You can only upload max : " + Contant.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        for (MultipartFile file: files){
            if(file.getSize() == 0){
                continue;
            }
            if (file.getSize() > 10*1024*1024){
                return CommonResult.failed("you can only upload file Maximum 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null && ! contentType.startsWith("image/")){
                return CommonResult.failed("you must up load file is image");
            }
            CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadImage(file);
        }
        return CommonResult.success("sac set");
    }

}
