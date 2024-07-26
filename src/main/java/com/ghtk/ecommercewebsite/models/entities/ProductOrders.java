package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Table(name = "product_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_order_id")
    private Long productOrderId;


    @Column(name = "orders_id", nullable = false)
    private Long ordersId;

    @Column(name = "product_item_id", nullable = false)
    private Long productItemId;


    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;


    private int quantity;
}
