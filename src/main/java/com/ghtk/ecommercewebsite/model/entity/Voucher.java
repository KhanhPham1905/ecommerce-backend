package com.ghtk.ecommercewebsite.model.entity;

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
    private long id;

    @Column(name = "coupon_code", columnDefinition = "VARCHAR(255)") 
    private String couponCode;

    @Column(name = "create_at", columnDefinition = "TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "discount_type", columnDefinition = "TINYINT")
    private int discountType;

    @Column(name = "discount_value", columnDefinition = "DECIMAL(12,2)")
    private BigDecimal discountValue;

    @Column(name = "expired_at", columnDefinition = "TIMESTAMP")
    private Timestamp expiredAt;

    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private Boolean isActive;

    @Column(name = "is_public", columnDefinition = "TINYINT(1)")
    private Boolean isPublic;

    @Column(name = "maximum_discount_value", columnDefinition = "DECIMAL(12,2)")
    private long maximumDiscountValue;

    @Column(name = "name", columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(name = "shop_id", columnDefinition = "BIGINT")
    private long shopId;

    @Column(name = "start_at", columnDefinition = "TIMESTAMP")
    private Timestamp startAt;

    @Column(name = "quantity", columnDefinition = "INT")
    private int quantity;

    @Column(name = "minimum_quantity_needed", columnDefinition = "INT")
    private int minimumQuantityNeeded;

    @Column(name = "type_repeat", columnDefinition = "TINYINT")
    private int typeRepeat;

}
