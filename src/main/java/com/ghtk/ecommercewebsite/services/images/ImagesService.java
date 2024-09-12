package com.ghtk.ecommercewebsite.services.images;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImagesService {
    void addImageProduct(CloudinaryResponse cloudinaryResponse, Long productId) throws Exception;
    void  addImageTextProduct(String text, Long id) throws DataNotFoundException;
    void insertNotDelete (String text, Long id) throws DataNotFoundException;
}
