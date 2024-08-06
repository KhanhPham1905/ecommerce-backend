package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
@Data
public class VoucherDTO {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Coupon code is required")
    @Size(max = 255, message = "Coupon code cannot exceed 255 characters")
    private String couponCode;

    @NotNull(message = "Discount type is required")
    private int discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    @NotNull(message = "Expiration date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredAt;

    @NotNull(message = "Active status is required")
    private boolean isActive;

    @NotNull(message = "Public status is required")
    private boolean isPublic;

    @NotNull(message = "Maximum discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum discount value must be greater than 0")
    private BigDecimal maximumDiscountValue;

    @NotBlank(message = "Name is required")
    @Size(max = 300, message = "Name cannot exceed 300 characters")
    private String name;

    @NotNull(message = "Shop ID is required")
    private Long shopId;

    @NotNull(message = "Start date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    @Positive(message = "Minimum quantity needed must be positive")
    private int minimumQuantityNeeded;

    @NotNull(message = "Type repeat is required")
    private boolean typeRepeat;
}
