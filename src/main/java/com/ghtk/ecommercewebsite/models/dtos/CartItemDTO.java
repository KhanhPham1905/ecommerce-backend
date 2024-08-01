package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDTO {

    @JsonProperty("id")
    @NotNull
    private Long id;

    @JsonProperty("cart_id")
    @NotNull
    private Long cartId;

    @JsonProperty("product_item_id")
    @NotNull
    private Long productItemId;

    @JsonProperty("quantity")
    private int quantity;
}