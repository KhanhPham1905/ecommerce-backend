package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;

@Entity
@Table(name = "product_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "sale_price", precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "product_id", nullable = false)
    private Long productId;
}
