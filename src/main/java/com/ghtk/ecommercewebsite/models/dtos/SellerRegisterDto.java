package com.ghtk.ecommercewebsite.models.dtos;

import com.ghtk.ecommercewebsite.models.enums.Gender;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerRegisterDto extends RegisterUserDto {

    private String tax;

    private String cccd;

    private Long shopId;

//    @Builder(builderClassName = "SellerRegisterDtoBuilder")
    public SellerRegisterDto(String email, String password, String fullName, String phone, Gender gender, String tax, String cccd, Long shopId) {
        super(email, password, fullName, phone, gender);
        this.tax = tax;
        this.cccd = cccd;
        this.shopId = shopId;
    }
}
