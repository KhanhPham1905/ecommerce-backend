package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "name", length = 300)
    private String name;

    private String slug;
    private int status;

    @Column(name = "total_sold")
    private Long totalSold;

    @Column(name = "product_view")
    private Integer productView;


    @Column(name = "brand_id", nullable = false)
    private Long brandId;


    @Column(name = "shop_id", nullable = false)
    private Long shopId;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
    }

    @Column(name = "is_delete")
    private Boolean isDelete;

    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
