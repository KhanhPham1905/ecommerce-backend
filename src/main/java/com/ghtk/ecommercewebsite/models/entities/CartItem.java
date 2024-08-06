package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_item_id", nullable = false)
    private Long productItemId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;


    @Column(name = "voucher_id")
    private Long voucherId;


    @Column(name = "shop_id", nullable = false)
    private Long shopId;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}
