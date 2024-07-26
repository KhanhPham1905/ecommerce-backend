package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ProductCategoryDTO {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Product ID is required")
    @JsonProperty("product_id")
    private Long productId;

    @NotNull(message = "Category ID is required")
    @JsonProperty("category_id")
    private Long categoryId;
}
