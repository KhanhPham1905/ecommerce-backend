package com.ghtk.ecommercewebsite.services.product;

import com.ghtk.ecommercewebsite.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final CloudinaryService cloudinaryService;
}
