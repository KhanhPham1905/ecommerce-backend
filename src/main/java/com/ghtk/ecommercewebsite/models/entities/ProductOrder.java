package com.ghtk.ecommercewebsite.models.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Data
@Entity
@Table(name="product_orders")
public class ProductOrder {

    @Id
    private Long id;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "orders_id")
    private Long ordersId;
 
}


