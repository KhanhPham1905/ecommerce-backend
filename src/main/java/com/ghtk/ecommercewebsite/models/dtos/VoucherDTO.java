package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VoucherDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 200, message = "Coupon code must be between 3 and 200 characters")
    @JsonProperty("coupon_code")
    private String couponCode;

    @Min(value = 0, message = "discount value must be greater than or equal to 0")
    @Max(value = 10000000, message = "discount value must be less than or equal to 10,000,000")
    @JsonProperty("discount_value")
    private BigDecimal discountValue;

    @JsonProperty("discount_type")
    private Integer discountType;

    @JsonProperty("expired_at")
    @NotBlank(message = "Expiration date is required")
    private LocalDateTime expiredAt;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("is_public")
    private Boolean isPublic;

    @JsonProperty("maximum_discount_value")
    @Min(value = 0, message = "maximum discount value must be greater than or equal to 0")
    @Max(value = 10000000, message = "maximum discount value must be less than or equal to 10,000,000")
    private BigDecimal maximumDiscountValue;

    @JsonProperty("name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @JsonProperty("shop_id")
    @Min(value=1, message = "Shop ID is required")
    private Long shopId;

    @JsonProperty("start_at")
    @NotBlank(message = "Start date is required")
    private LocalDateTime startAt;

    @JsonIgnore
    private LocalDateTime createdAt;

    @JsonProperty("quantity")
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @Max(value = 10000000, message = "Quantity must be less than or equal to 10,000,000")
    private Integer quantity;

    @JsonProperty("minimum_quantity_needed")
    @NotNull(message = "Minimum quantity needed is required")
    private Integer minimumQuantityNeeded;

    @JsonProperty("type_repeat")
    @NotNull(message = "type repeat needed is required")
    private Boolean typeRepeat;
}
