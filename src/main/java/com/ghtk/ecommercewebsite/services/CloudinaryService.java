package com.ghtk.ecommercewebsite.services;

import com.cloudinary.Cloudinary;
import com.ghtk.ecommercewebsite.models.responses.CloudinaryResponse;
import com.ghtk.ecommercewebsite.utils.FileUploadUtil;
import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;
    private final SnowflakeIdGenerator snowflakeIdGenerator;

    @Transactional
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName) throws Exception {
        try {
            Map result = cloudinary.uploader().upload(file.getBytes(), Map.of("public_id",  fileName));
            String url = (String) result.get("secure_url");
            String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder()
                    .publicId(publicId)
                    .url(url)
                    .build();
        } catch (Exception e) {
            throw new Exception("Failed to upload file");
        }
    }

    public CloudinaryResponse uploadImage( MultipartFile file) throws  Exception{
        FileUploadUtil.assertAllowed(file, FileUploadUtil.IMAGE_PATTERN);
        Long snowFlakeId = snowflakeIdGenerator.next();
        String fileName = FileUploadUtil.getFileName(snowFlakeId);
        CloudinaryResponse response = uploadFile(file, fileName);
        return  response;
    }
}
