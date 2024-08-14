package com.ghtk.ecommercewebsite.models.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Data
public class ProductDTO {

    @JsonProperty("id")
    private Long id;
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Name is required")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    private String name;

    private int status;

    private String slug;

    private Long totalSold;

    private Integer productView;

    private Boolean isDelete;

    @NotNull(message = "Brand ID is required")
    private Long brandId;

//    @NotNull(message = "Shop ID is required")
    private Long shopId;

    private List<Long> categoryIds;

    private List<MultipartFile> images;

    private MultipartFile thumbnail;

    private String thumbnailImg ;


}
