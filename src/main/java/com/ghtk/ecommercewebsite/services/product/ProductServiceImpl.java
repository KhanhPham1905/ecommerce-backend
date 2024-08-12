package com.ghtk.ecommercewebsite.services.product;


import com.ghtk.ecommercewebsite.configs.Contant;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public Page<Product> searchProducts(
            String categoryId, String brandId,
            String keyword,
            Long userId,
            PageRequest pageRequest
    ) {
        Shop shop = shopRepository.findByUserId(userId);
        Page<Product> productsPage;
        productsPage = productsRepository.searchProducts(shop.getId(),brandId.equals("")? null :Long.parseLong(brandId), keyword, pageRequest);
//        List<Category> categories =
        return productsPage;
    }
}
