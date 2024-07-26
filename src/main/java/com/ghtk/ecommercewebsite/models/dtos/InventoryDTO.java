package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class InventoryDTO {

    @JsonProperty("id")
    private Long id;

    @NotNull(message = "Quantity is required")
    @JsonProperty("quantity")
    private int quantity;

    @NotNull(message = "Product Item ID is required")
    @JsonProperty("product_item_id")
    private Long productItemId;

    @NotNull(message = "Warehouse ID is required")
    @JsonProperty("warehouse_id")
    private Long warehouseId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
