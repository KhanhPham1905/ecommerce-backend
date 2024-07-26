package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductAttributesDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 45, message = "Name cannot exceed 45 characters")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Product ID is required")
    @JsonProperty("product_id")
    private Long productId;
}
