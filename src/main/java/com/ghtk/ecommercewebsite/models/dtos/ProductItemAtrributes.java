package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductItemAtrributes {

    @NotBlank(message = "Value cannot be blank")
    @JsonProperty("value")
    private String value;

    @Min(value=1, message =  "Product attributes ID is required")
    @JsonProperty("product_attributes_id")
    private Long productAttributesId;

    @Min(value=1, message =  "Product item ID is required")
    @JsonProperty("product_item_id")
    private Long productItemId;
}
