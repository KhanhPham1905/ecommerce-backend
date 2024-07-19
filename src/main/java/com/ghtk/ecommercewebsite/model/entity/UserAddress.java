package com.ghtk.ecommercewebsite.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name="user_address")
public class UserAddress implements Serializable {
    @EmbeddedId
    private UserAddressId userAddressId;
}