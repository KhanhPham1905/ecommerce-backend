package com.ghtk.ecommercewebsite.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String mail;
    private String phone;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    // Getters and setters
}
