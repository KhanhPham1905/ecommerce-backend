package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String status;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    // Getters and setters
}
