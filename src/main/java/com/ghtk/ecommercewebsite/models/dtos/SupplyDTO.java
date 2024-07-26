package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SupplyDTO {

    @JsonProperty("supply_date")
    private LocalDateTime supplyDate;

    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @JsonProperty("quantity")
    private int quantity;

    @NotNull(message = "Warehouse ID is required")
    @JsonProperty("warehouse_id")
    private Long warehouseId;

    @NotNull(message = "Product Item ID is required")
    @JsonProperty("product_item_id")
    private Long productItemId;
}
