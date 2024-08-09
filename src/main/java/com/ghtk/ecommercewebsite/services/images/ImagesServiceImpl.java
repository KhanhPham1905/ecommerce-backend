package com.ghtk.ecommercewebsite.services.images;

import com.ghtk.ecommercewebsite.models.entities.Image;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.repositories.ImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImagesServiceImpl implements ImagesService{
    private  final ImagesRepository imagesRepository;
    @Override
    public void addImageProduct(CloudinaryResponse cloudinaryResponse, Long productId) throws Exception {
        Image image = Image.builder()
                .productId(productId)
                .link(cloudinaryResponse.getUrl())
                .name(cloudinaryResponse.getPublicId())
                .build();
        imagesRepository.save(image);
    }
}
