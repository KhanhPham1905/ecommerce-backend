package com.ghtk.ecommercewebsite.models.dtos;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CartItemDTO {

    private Long id;
    private Long userId;
    private Long productItemId;
    private int quantity;
    private Long voucherId;


    private Boolean status = true;
    private Long shopId;
}
