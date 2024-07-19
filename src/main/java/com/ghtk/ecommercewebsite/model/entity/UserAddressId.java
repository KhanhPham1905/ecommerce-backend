package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class UserAddressId{
    @Column(name = "address_id", columnDefinition = "BIGINT")
    private long addressId;

    @Column(name = "users_id", columnDefinition = "BIGINT")
    private long usersId;

}