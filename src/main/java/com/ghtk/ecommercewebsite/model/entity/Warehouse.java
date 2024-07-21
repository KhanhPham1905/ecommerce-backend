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

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;
    
    @Column(name = "shop_id", columnDefinition = "BIGINT")
    private Long shopId;

    @Column(name = "address_id", columnDefinition = "BIGINT")
    private Long addressId;
}
