package com.ghtk.ecommercewebsite.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Entity
@Table(name="product_orders")
public class ProductOrder {

    @Id
    private Long id;

    @Column(name = "price", columnDefinition = "BIGINT")
    private BigDecimal price;

    @Column(name = "quantity", columnDefinition = "INT")
    private Integer quantity;

    @Column(name = "orders_id", columnDefinition = "BIGINT")
    private Long ordersId;
 
}


