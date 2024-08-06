package com.ghtk.ecommercewebsite.configs;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary(){
        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dqgarzqlx");
        config.put("api_key","889522243963762");
        config.put("api_secret", "uMzkNQ_5w09qZ3r-WftTWjSOZAE");
        return new Cloudinary(config);
    }
}
