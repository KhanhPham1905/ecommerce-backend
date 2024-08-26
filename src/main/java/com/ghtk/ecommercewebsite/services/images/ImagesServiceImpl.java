package com.ghtk.ecommercewebsite.services.images;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.entities.Image;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.repositories.ImagesRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;

@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService{
    private  final ImagesRepository imagesRepository;
    private  final ProductRepository productsRepository;
    @Override
    public void addImageProduct(CloudinaryResponse cloudinaryResponse, Long productId) throws Exception {
        Image image = Image.builder()
                .productId(productId)
                .link(cloudinaryResponse.getUrl())
                .name(cloudinaryResponse.getPublicId())
                .build();
        imagesRepository.save(image);
    }

    @Override
    @Transactional
    public void addImageTextProduct(String img, Long id) throws DataNotFoundException{
        imagesRepository.deleteByLink(img);
        Image image = Image.builder()
                .link(img)
                .productId(id)
                .build();
        imagesRepository.save(image);
    }
}
