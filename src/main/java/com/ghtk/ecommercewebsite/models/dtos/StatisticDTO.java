package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StatisticDTO {
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("profit")
    private BigDecimal profit;

    @JsonProperty("sales")
    private BigDecimal sales;

    @Min(value=1, message = "Order ID is required")
    @JsonProperty("order_id")
    private Long orderId;

    @Min(value=1, message = "Shop ID is required")
    @JsonProperty("shop_id")
    private Long shopId;
}
