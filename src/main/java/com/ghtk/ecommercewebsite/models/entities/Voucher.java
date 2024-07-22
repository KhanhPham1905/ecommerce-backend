package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;

@Data
@Entity
@Table(name="voucher")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_code")
    private String couponCode;

    @Column(name = "create_at")
    private Timestamp createdAt;

    @Column(name = "discount_type")
    private Integer discountType;

    @Column(name = "discount_value")
    private BigDecimal discountValue;

    @Column(name = "expired_at")
    private Timestamp expiredAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "maximum_discount_value")
    private Long maximumDiscountValue;

    @Column(name = "name")
    private String name;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "start_at")
    private Timestamp startAt;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "minimum_quantity_needed")
    private Integer minimumQuantityNeeded;

    @Column(name = "type_repeat")
    private Integer typeRepeat;
}
