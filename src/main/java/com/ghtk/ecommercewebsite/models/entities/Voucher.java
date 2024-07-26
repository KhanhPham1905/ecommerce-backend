package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_code", nullable = false, unique = true)
    private String couponCode;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime modifiedAt;

    @Column(name = "discount_type", nullable = false)
    private int discountType;

    @Column(name = "discount_value", precision = 12, scale = 2, nullable = false)
    private BigDecimal discountValue;


    @Column(name = "expired_at", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime expiredAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic;

    @Column(name = "maximum_discount_value", nullable = false)
    private BigDecimal maximumDiscountValue;

    @Column(name = "name", length = 300, nullable = false)
    private String name;

    // Khóa ngoại
    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "start_at", nullable = false, columnDefinition = "DATETIME(6)")
    private LocalDateTime startAt;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "minimum_quantity_needed", nullable = false)
    private int minimumQuantityNeeded;

    @Column(name = "type_repeat", nullable = false)
    private boolean typeRepeat;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }


}