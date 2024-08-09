package com.ghtk.ecommercewebsite.models.dtos.request;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    private Long userId;
    private boolean method;
    private Long addressID;
    private String note;

    // Getter và Setter
}