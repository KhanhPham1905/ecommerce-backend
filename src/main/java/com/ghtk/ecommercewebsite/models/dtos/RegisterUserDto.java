package com.ghtk.ecommercewebsite.models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterUserDto {
    private String email;

    private String password;

    private String fullName;
}
