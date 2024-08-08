package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

public class ProductOrdersDTO {

    @Min(value=1, message =  "Orders ID is required")
    @JsonProperty("orders_id")
    private Long ordersId;

    @Min(value=1, message =  "Product item ID is required")
    @JsonProperty("product_item_id")
    private Long productItemId;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private int quantity;
}
