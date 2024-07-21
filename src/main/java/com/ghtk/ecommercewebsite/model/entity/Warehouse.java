package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "address_id")
    private Long addressId;
}
