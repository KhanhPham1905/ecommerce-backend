package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;

@Data
@Entity
@Table(name = "statistic")
public class Statistic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "profit", columnDefinition = "DECIMAL(17,2)")
    private BigDecimal profit;

    @Column(name = "sales", columnDefinition = "DECIMAL(17,2)")
    private BigDecimal sales;

    @Column(name = "order_id", columnDefinition = "BIGINT")
    private Long ordersId;

    @Column(name = "shop_id", columnDefinition = "DECIMAL(17,2)")
    private Long shopId;
}

