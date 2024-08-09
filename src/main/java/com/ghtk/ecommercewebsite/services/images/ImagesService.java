package com.ghtk.ecommercewebsite.services.images;

import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    void addImageProduct(CloudinaryResponse cloudinaryResponse, Long productId) throws Exception;
}
