package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class RateDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("average_stars")
    private BigDecimal averageStars;

    @JsonProperty("quantity")
    private Long quantity;

    @NotNull(message = "Product ID is required")
    @JsonProperty("product_id")
    private Long productId;
}
