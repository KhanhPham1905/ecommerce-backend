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

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "profit")
    private BigDecimal profit;

    @Column(name = "sales")
    private BigDecimal sales;

    @Column(name = "order_id")
    private Long ordersId;

    @Column(name = "shop_id")
    private Long shopId;
}

