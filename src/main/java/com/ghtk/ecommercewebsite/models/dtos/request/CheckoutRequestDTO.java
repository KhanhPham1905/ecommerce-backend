package com.ghtk.ecommercewebsite.models.dtos.request;

import lombok.Data;

@Data
public class CheckoutRequestDTO {
    private boolean method;
    private String note;
    // Getter và Setter
}