package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdersDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("modified_at")
    private LocalDateTime modifiedAt;

    @JsonProperty("note")
    private String note;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("receiver_phone")
    private String receiverPhone;

    @NotNull(message = "Status is required")
    @JsonProperty("status")
    private OrderStatus status;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be positive")
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    @JsonProperty("voucher_id")
    private Long voucherId;

    @JsonProperty("buyer")
    private Long buyer;

    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("modified_by")
    private Long modifiedBy;

    @NotNull(message = "Shop ID is required")
    @JsonProperty("shop_id")
    private Long shopId;

    @JsonProperty("method")
    private boolean method;

    private Long userId;

    private Long addressID;


    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}
