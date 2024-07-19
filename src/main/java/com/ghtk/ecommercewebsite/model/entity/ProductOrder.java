package com.ghtk.ecommercewebsite.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Entity
@Table(name="product_orders")
public class ProductOrder {

    @EmbeddedId
    private ProductOrderId productOrderId;

    @Column(name = "price", columnDefinition = "BIGINT")
    private BigDecimal price;

    @Column(name = "quantity", columnDefinition = "INT")
    private int quantity;

    @Column(name = "orders_id", columnDefinition = "BIGINT")
    private long ordersId;
 
}


