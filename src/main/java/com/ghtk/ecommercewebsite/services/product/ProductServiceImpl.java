package com.ghtk.ecommercewebsite.services.product;


import com.ghtk.ecommercewebsite.configs.Contant;
import com.ghtk.ecommercewebsite.mapper.ProductMapper;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.services.productitem.ProductItemServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.services.CloudinaryService;
import com.ghtk.ecommercewebsite.services.images.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Product> findAll() {
        return productsRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productsRepository.findById(id);
    }



    @Transactional
    public Product save(ProductDTO productDTO) throws  Exception {
        Product product = productsRepository.save(productMapper.toEntity(productDTO));
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


}
