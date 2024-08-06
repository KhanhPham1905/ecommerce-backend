package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ProductController {

//    private final CloudinaryService cloudinaryService;
//    @PostMapping("/image/{id}")
//    public ResponseEntity<?> uploadImage(@PathVariable final Integer id, @RequestPart final MultipartFile file) {
//        cloudinaryService.uploadImage(id, file);
//        return ResponseEntity.ok("Upload successfully");
//    }
}
