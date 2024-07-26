package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("quantity")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @JsonProperty("size")
    private int size;

    @JsonProperty("user_id")
    @NotNull(message = "User ID is required")
    private Long userId;

    @JsonProperty("product_item_id")
    @NotNull(message = "Product Item ID is required")
    private Long productItemId;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
