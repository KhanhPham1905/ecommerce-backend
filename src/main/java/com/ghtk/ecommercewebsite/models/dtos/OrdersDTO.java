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

    private Long id;

    private String note;

    @NotNull(message = "Status is required")
    private OrderStatus status;

    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be positive")
    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    private boolean method;

    private Long userId;

    private Long addressID;

    @JsonProperty("address")
    private String address;

    @JsonProperty("address_detail")
    private String addressDetail;

    @JsonProperty("receiver_phone")
    private String receiverPhone;

    @JsonProperty("buyer")
    private String buyer;

    public enum OrderStatus {
        PENDING,
        COMPLETED,
        CANCELLED
    }
}
