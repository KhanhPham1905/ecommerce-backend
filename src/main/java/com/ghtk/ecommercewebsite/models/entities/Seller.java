package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "seller")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tax;

    private String cccd;

    @Enumerated(EnumType.STRING)
    @Column(name = "deliverytype", nullable = false)
    private DeliveryType deliverytype;



    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "user_id", nullable = false)
    private Long userId;



    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum DeliveryType {
        STANDARD,
        EXPRESS,
        OVERNIGHT
    }
}
