package com.ghtk.ecommercewebsite.controllers;

import com.cloudinary.Cloudinary;
import com.ghtk.ecommercewebsite.configs.Contant;
import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ListAttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.models.responses.ProductListResponse;
import com.ghtk.ecommercewebsite.models.responses.ProductResponse;
import com.ghtk.ecommercewebsite.services.CloudinaryService;
import com.ghtk.ecommercewebsite.services.images.ImagesService;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final IProductService iProductService;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;


    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult<ProductListResponse> getAllProductsUser(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "", name = "category-ids") String categoryIds,
            @RequestParam(defaultValue = "", name = "brand-ids") String brandIds,
            @RequestParam(defaultValue = "default", name ="sort") String sortOption,
            @RequestParam(required = false, name = "fromPrice") Long fromPrice,
            @RequestParam(required = false, name = "toPrice") Long toPrice,
            @RequestParam(defaultValue = "", name ="rate") Float rate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit
    ) throws Exception{
        Sort sort = switch (sortOption) {
            case "latest" -> Sort.by("createdAt").descending();
            case "desc" -> Sort.by("minPrice").descending();
            case "asc" -> Sort.by("minPrice").ascending();
            default -> Sort.by("id").ascending();
        };

        PageRequest pageRequest = PageRequest.of(
                page, limit,
//                Sort.by("createdAt").descending());
                sort);
        Page<ProductResponse> productPage = null;
        List<Long> brandList = null;
        if (!brandIds.equals("")) {
            brandList = Arrays.stream(brandIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (brandList.isEmpty()) {
                brandList = null;
            }
        }

        List<Long> categoryList = null;
        if (!categoryIds.equals("")) {
            categoryList = Arrays.stream(categoryIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (categoryList.isEmpty()) {
                categoryList = null;
            }
        }

        int totalPages = 0;
        List<ProductResponse> productResponses = null;
        if (productPage == null) {
            productPage = iProductService.searchProducts(categoryList, categoryList == null ? 0 : categoryList.size(), brandList, keyword, fromPrice , toPrice ,rate, pageRequest);
            // Get total pages
            totalPages = productPage.getTotalPages();
            productResponses = productPage.getContent();
        }

        return CommonResult.success(ProductListResponse.builder()
                .productResponses(productResponses)
                .totalPages(totalPages)
                .build(), "Get all products successfully");
    }

    @GetMapping("/seller")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<ProductListResponse> getAllProductsAdmin(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "", name = "category-ids") String categoryIds,
            @RequestParam(defaultValue = "", name = "brand-ids") String brandIds,
            @RequestParam(defaultValue = "default", name ="sort") String sortOption,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "16") int limit
    ) throws Exception{
        Sort sort = switch (sortOption) {
            case "latest" -> Sort.by("createdAt").descending();
            case "desc" -> Sort.by("minPrice").descending();
            case "asc" -> Sort.by("minPrice").ascending();
            default -> Sort.by("id").ascending();
        };

        PageRequest pageRequest = PageRequest.of(
                page, limit,
//                Sort.by("createdAt").descending());
                sort);
        Page<ProductResponse> productPage = null;
        List<Long> brandList = null;
        if (!brandIds.equals("")) {
            brandList = Arrays.stream(brandIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (brandList.isEmpty()) {
                brandList = null;
            }
        }

        List<Long> categoryList = null;
        if (!categoryIds.equals("")) {
            categoryList = Arrays.stream(categoryIds.split(","))
                    .map(Long::parseLong)
                    .toList();
            if (categoryList.isEmpty()) {
                categoryList = null;
            }
        }

        int totalPages = 0;
        List<ProductResponse> productResponses = null;
        if (productPage == null) {
            productPage = iProductService.searchProductsSeller(categoryList, categoryList == null ? 0 : categoryList.size(), brandList, keyword, pageRequest);
            // Get total pages
            totalPages = productPage.getTotalPages();
            productResponses = productPage.getContent();
        }

        return CommonResult.success(ProductListResponse.builder()
                .productResponses(productResponses)
                .totalPages(totalPages)
                .build(), "Get all products successfully");
    }


    @GetMapping("/{id}")
    public CommonResult<ProductResponse> getProductById(@PathVariable Long id) throws Exception{
        ProductResponse productResponse = iProductService.getProductById(id);
        return CommonResult.success(productResponse, "Get product by id success");
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<ProductDTO> createProduct(@ModelAttribute ProductDTO productDTO) throws Exception{
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product savedProduct = iProductService.save(productDTO, user.getId());
        return CommonResult.success(null, "Create product successfully");
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<ProductDTO> updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO) throws Exception{
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product savedProduct = iProductService.updateProductById(id,productDTO, user.getId());
        return CommonResult.success(null,"Update product successfully");
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
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<String> deleteProduct(@PathVariable Long id) throws Exception{
        iProductService.deleteById(id);
        return CommonResult.success("delete product success");
    }

//    @GetMapping("/searchName")
//    public CommonResult<List<ProductDTO>> searchProductsByName(@RequestParam("keyword") String keyword) {
//        List<ProductDTO> products = iProductService.searchProductsByName(keyword).stream()
//                .map(productMapper::toDTO)
//                .collect(Collectors.toList());
//        if (products.isEmpty()) return CommonResult.error( 404 , "notfounbd");
//        else return CommonResult.success(products, "Search products by name successfully");
//    }
//
//    @GetMapping("/searchDes")
//    public CommonResult<List<ProductDTO>> searchProductsByDes(@RequestParam("keyword") String keyword) {
//        List<ProductDTO> products = iProductService.searchProductsByDes(keyword).stream()
//                .map(productMapper::toDTO)
//                .collect(Collectors.toList());
//        return CommonResult.success(products, "Search products by description successfully");
//    }

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
