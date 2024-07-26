package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WarehouseDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Name must be between 3 and 200 characters")
    private String name;

    @JsonProperty("shop_id")
    @Min(value=1, message = "shop id needed is required")
    private Long shopId;

    @JsonProperty("address_id")
    @Min(value=1, message =  "address id needed is required")
    private Long addressId;
}
