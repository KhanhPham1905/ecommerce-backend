package com.ghtk.ecommercewebsite.models.dtos.request;

import lombok.Data;

@Data
public class CheckoutDirectRequestDTO {
    private Long userId;
    private Long addressID;
    private Long productItemId;
    private int quantity;
    private Long voucherId;
    private String note;
    private boolean method;

    // Getter và Setter
}