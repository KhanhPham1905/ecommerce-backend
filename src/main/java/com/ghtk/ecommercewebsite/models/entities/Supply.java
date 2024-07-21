package com.ghtk.ecommercewebsite.models.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.security.Timestamp;

@Data
@Entity
@Table(name="supply")
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supply_date")
    private Timestamp supplyDate;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "warehouse_id")
    private Long wareHouseId;

    @Column(name = "product_item_id")
    private Long productItemId;

}
