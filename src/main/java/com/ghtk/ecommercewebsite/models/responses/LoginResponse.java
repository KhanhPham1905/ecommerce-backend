package com.ghtk.ecommercewebsite.models.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;
}
