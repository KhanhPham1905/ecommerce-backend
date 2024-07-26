package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

public class ProductDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;

    @NotBlank(message = "Description is required")
    @JsonProperty("description")
    private String description;

    @NotBlank(message = "Name is required")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    @JsonProperty("name")
    private String name;

    @JsonProperty("slug")
    private String slug;

    @NotNull(message = "Status is required")
    @JsonProperty("status")
    private Integer status;

    @JsonProperty("total_sold")
    private Long totalSold;

    @JsonProperty("product_view")
    private Integer productView;

    @NotNull(message = "Brand ID is required")
    @JsonProperty("brand_id")
    private Long brandId;

    @NotNull(message = "Shop ID is required")
    @JsonProperty("shop_id")
    private Long shopId;
}

