package com.ghtk.ecommercewebsite.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;




@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    @JsonProperty("id")
    private Long id;
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Name is required")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    private String name;

    private Boolean status;

    private String slug;

    private Long totalSold;

    private Integer productView;

    private String brand;

    private List<String> categories;

    private String images;
}
