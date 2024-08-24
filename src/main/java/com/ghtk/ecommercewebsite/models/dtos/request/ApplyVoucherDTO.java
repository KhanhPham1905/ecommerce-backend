package com.ghtk.ecommercewebsite.models.dtos.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApplyVoucherDTO {
    private Long cartItemId;
    private Long voucherId;
}
