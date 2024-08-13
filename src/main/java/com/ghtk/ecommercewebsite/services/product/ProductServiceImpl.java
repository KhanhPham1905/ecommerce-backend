package com.ghtk.ecommercewebsite.services.product;


import com.ghtk.ecommercewebsite.configs.Contant;
import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.CategoryDTO;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.models.responses.ProductResponse;
import com.ghtk.ecommercewebsite.repositories.*;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.services.productitem.ProductItemServiceImpl;
import com.ghtk.ecommercewebsite.services.shop.ShopService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.services.CloudinaryService;
import com.ghtk.ecommercewebsite.services.images.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final CloudinaryService cloudinaryService;
    private final ProductRepository productsRepository;
    private final ProductItemServiceImpl productItemService;
    private final ProductItemRepository productItemRepository;
    private final ProductMapper productMapper;
    private final ImagesService imagesService;
    private final CategoryProductRepository categoryProductRepository ;
    private final ShopRepository shopRepository;
    private final BrandRepository brandRepository;
    private final RateRepository rateRepository;
    private final ImagesRepository imagesRepository;

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productsRepository.findById(id);
    }


    @Transactional
    public Product save(ProductDTO productDTO, Long userId) throws  Exception {
        Shop shop = shopRepository.findByUserId(userId);
        productDTO.setShopId(shop.getId());
        Product product = productsRepository.save(productMapper.toEntity(productDTO));
        for (int i = 0; i < productDTO.getCategoryIds().size(); i++){
            ProductCategory productCategory = ProductCategory.builder()
                    .categoryId(productDTO.getCategoryIds().get(i))
                    .productId(product.getId())
                    .build();

            categoryProductRepository.save(productCategory);
        }
        List<MultipartFile> files = productDTO.getImages();
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if(files.size() > Contant.MAXIMUM_IMAGES_PER_PRODUCT){
            new Exception("You can only upload max : " + Contant.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        for (MultipartFile file: files){
            if(file.getSize() == 0){
                continue;
            }
            if (file.getSize() > 2*1024*1024){
                new Exception("you can only upload file Maximum 10MB");
            }
            String contentType = file.getContentType();
            if (contentType == null && ! contentType.startsWith("image/")){
                new Exception("you must up load file is image");
            }
            CloudinaryResponse cloudinaryResponse = cloudinaryService.uploadImage(file);
            imagesService.addImageProduct(cloudinaryResponse, product.getId());
        }
        return product;
    }


    @Transactional
    public void deleteById(Long id) {
        productItemService.deleteProductItemById(id);
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

    @Override
    public List<Product> getAllProducts() throws Exception{
//        List<Product> products = productsRepository.findAll();
//        List<ProductResponse> productResponses = new ArrayList<>();
//        for (Product product : products){
////            Brand brand = brandRepository.findById(product.getBrandId())
////                    .orElse();
//            ProductResponse productResponse = ProductResponse.builder()
//                    .brand()
//                    .build();
//        }
        return List.of();
    }

//    @Override
//    public Page<ProductResponse> searchProducts(
//            List<Long> categoryIds,
//            long categoryCount,
//            List<Long> brandIds,
//            String keyword,
//            PageRequest pageRequest
//    ) {
//
//        Page<Product> productsPage;
//        productsPage = productsRepository.searchProducts(categoryIds, categoryCount,brandIds, keyword, pageRequest);
//
//        List<String> categoryNames = product.getCategoryList().stream()
//                .map(Category::getName).toList();
//        ProductResponse productResponse = ProductResponse.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .description(product.getDescription())
//                .brandName(product.)
//                .build()
//        productsPage.map(
//
//        );

//
//        return productsPage.map(ProductResponse::convertToProductResponse);
//    }
    @Override
    public Page<ProductResponse> searchProducts(
            List<Long> categoryIds,
            long categoryCount,
            List<Long> brandIds,
            String keyword,
            PageRequest pageRequest
    ) throws Exception {
        return productsRepository.searchProducts(categoryIds, categoryCount, brandIds, keyword, pageRequest)
            .map(product -> {
                List<String> categoryNames = product.getCategoryList().stream()
                        .map(Category::getName)
                        .collect(Collectors.toList());

                Rate rate = rateRepository.findByProductId(product.getId());
                // Xử lý Optional Brand để tránh NoSuchElementException
                Optional<Brand> brand = brandRepository.findById(product.getBrandId());
                List<String> imageList = imagesRepository.findLinkByProductId(product.getId());

                return ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .thumbnail(product.getThumbnail())
                        .averageRate(rate.getAverageStars())
                        .brandId(brand.get().getId())
                        .brandName(brand.get().getName())
                        .images(imageList)
                        .categoryNames(categoryNames)
                        .categories(product.getCategoryList())
                        .createdAt(product.getCreatedAt())
                        .modifiedAt(product.getModifiedAt())
                        .build();
            });
    }

}
