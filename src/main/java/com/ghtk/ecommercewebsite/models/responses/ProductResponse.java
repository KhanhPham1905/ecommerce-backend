package com.ghtk.ecommercewebsite.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.Product;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {

    @JsonProperty("id")
    private Long id;
    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Name is required")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    private String name;

    private Integer status;

    private String slug;

    @JsonProperty("total_sold")
    private Long totalSold;

    @JsonProperty("product_view")
    private Integer productView;

    @JsonProperty("brand_id")
    private Long brandId;

    @JsonProperty("brand_name")
    private String brandName;

    @JsonProperty("categories")
    private List<Category> categories;

    @JsonProperty("category_names")
    private List<String> categoryNames;

    private List<String> images;

    private String thumbnail;

    @JsonProperty("average_rate")
    private BigDecimal averageRate;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdAt;

    @JsonProperty("modified_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime modifiedAt;


    @JsonProperty("min_price")
    private BigDecimal minPrice;

    @JsonProperty("quantity_rate")
    private Long quantityRate;

}


