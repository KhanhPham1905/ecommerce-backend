package com.ghtk.ecommercewebsite.model.entity;


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

    @Column(name = "supply_date", columnDefinition = "TIMESTAMP")
    private Timestamp supplyDate;

    @Column(name = "quantity", columnDefinition = "INT")
    private Long quantity;

    @Column(name = "warehouse_id", columnDefinition = "BIGINT")
    private Long wareHouseId;

    @Column(name = "product_item_id", columnDefinition = "DECIMAL(17,2)")
    private Long productItemId;

}
