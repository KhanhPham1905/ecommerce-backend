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

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.*;
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
    private final ProductAttributesRepository productAttributesRepository;
    private final AttributeValuesRepository attributeValuesRepository;

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
        productDTO.setIsDelete(Boolean.FALSE);
//        CloudinaryResponse ThumbcloudinaryResponse = cloudinaryService.uploadImage(productDTO.getThumbnail());
//        productDTO.setThumbnailImg(ThumbcloudinaryResponse.getUrl());
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
                new Exception("you can only upload file Maximum 2MB");
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
    public void deleteById(Long id) throws Exception{
        Product product = productsRepository.findById(id)
                .orElseThrow(()->  new DataNotFoundException("Cannot find product by id"));
        product.setIsDelete(Boolean.TRUE);

        productsRepository.save(product);
        productAttributesRepository.softDeleteProductAttributesByProductId(id);
        productItemRepository.softDeleteProductItemByProductId(id);
    }


    @Override
    public List<Product> findByBrandId(Long brandId) {
        return productsRepository.findByBrandId(brandId);
    }

//    @Override
//    public void deleteBrandById(Long brandId) {
//        List<Product> products = findByBrandId(brandId);
//        for (Product product : products) {
//            deleteById(product.getId());
//        }
//    }

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
        return List.of();
    }

    @Override
    public Page<ProductResponse> searchProducts(
            List<Long> categoryIds,
            long categoryCount,
            List<Long> brandIds,
            String keyword,
            Long fromPrice,
            Long toPrice,
            Float rateStar,
            PageRequest pageRequest
    ) throws Exception {
        return productsRepository.searchProducts(categoryIds, brandIds, keyword, fromPrice , toPrice, rateStar, pageRequest)
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
                        .minPrice(product.getMinPrice())
                        .totalSold(product.getTotalSold())
                        .thumbnail(product.getThumbnail())
                        .averageRate(rate == null? BigDecimal.valueOf(0):rate.getAverageStars())
                        .brandId(brand.get().getId())
                        .brandName(brand.get().getName())
                        .quantityRate(rate==null?0:rate.getQuantity())
                        .images(imageList)
                        .categoryNames(categoryNames)
                        .categories(product.getCategoryList())
                        .createdAt(product.getCreatedAt())
                        .modifiedAt(product.getModifiedAt())
                        .build();
            });
    }

    @Override
    public Page<ProductResponse> searchProductsSeller(
            List<Long> categoryIds,
            long categoryCount,
            List<Long> brandIds,
            String keyword,
            PageRequest pageRequest
    ) throws Exception {
        return productsRepository.searchProductsSeller(categoryIds, categoryCount, brandIds, keyword, pageRequest)
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
                            .minPrice(product.getMinPrice())
                            .totalSold(product.getTotalSold())
                            .description(product.getDescription())
                            .thumbnail(product.getThumbnail())
                            .averageRate(rate == null? BigDecimal.valueOf(0):rate.getAverageStars())
                            .brandId(brand.get().getId())
                            .brandName(brand.get().getName())
                            .quantityRate(rate==null?0:rate.getQuantity())
                            .images(imageList)
                            .categoryNames(categoryNames)
                            .categories(product.getCategoryList())
                            .createdAt(product.getCreatedAt())
                            .modifiedAt(product.getModifiedAt())
                            .build();
                });
    }

    @Override
    @Transactional
    public Product updateProductById(Long id, ProductDTO productDTO, Long userId) throws Exception {
        Product product = productsRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find product"));

        Shop shop = shopRepository.findByUserId(userId);
        if(!product.getShopId().equals(shop.getId())){
            throw new AccessDeniedException("you do not have access");
        }

        productDTO.setShopId(shop.getId());
        productDTO.setId(product.getId());

        imagesRepository.deleteByProductId(product.getId());
//        CloudinaryResponse ThumbcloudinaryResponse = cloudinaryService.uploadImage(productDTO.getThumbnail());
//        productDTO.setThumbnailImg(ThumbcloudinaryResponse.getUrl());
        productsRepository.save(productMapper.toEntity(productDTO));

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
                new Exception("you can only upload file Maximum 2MB");
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


    @Override
    public ProductResponse getProductById(Long id) throws Exception {
        Product product = productsRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("product not found"));
        Rate rate = rateRepository.findByProductId(id);
        Optional<Brand> brand = brandRepository.findById(product.getBrandId());
        List<String> imageList = imagesRepository.findLinkByProductId(product.getId());
        List<ProductAttributes> productAttributes= productAttributesRepository.findAllByProductId(id);
        ArrayList<Object> attributeAndValues = new ArrayList<>();
        for(ProductAttributes productAttribute : productAttributes){
            Map<String,Object> result = new HashMap<>();
            result.put("id",productAttribute.getId());
            result.put("attribute",productAttribute.getName());
            result.put("values",attributeValuesRepository.findAttributeValuesByAttributeId(productAttribute.getId()));
            attributeAndValues.add(result);
        }
        Long quantity = productItemRepository.getQuantityProduct(id);
        return ProductResponse.builder()
                .attributeAndValues(attributeAndValues)
                .id(product.getId())
                .quantity(quantity)
                .name(product.getName())
                .status(product.getStatus())
                .totalSold(product.getTotalSold())
                .minPrice(product.getMinPrice())
                .description(product.getDescription())
                .thumbnail(product.getThumbnail())
                .averageRate(rate == null? BigDecimal.valueOf(0):rate.getAverageStars())
                .brandId(brand.get().getId())
                .images(imageList)
                .brandName(brand.get().getName())
                .quantityRate(rate==null?0:rate.getQuantity())
                .categories(product.getCategoryList())
                .createdAt(product.getCreatedAt())
                .modifiedAt(product.getModifiedAt())
                .build();
    }
}
