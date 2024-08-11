package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductItemDTO {

    private Long id;

    @PositiveOrZero(message = "Price must be zero or positive")
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("sale_price")
    private BigDecimal salePrice;

    @Min(value = 1, message = "Product ID is required")
    @JsonProperty("product_id")
    private Long productId;

    private Boolean status;
}
