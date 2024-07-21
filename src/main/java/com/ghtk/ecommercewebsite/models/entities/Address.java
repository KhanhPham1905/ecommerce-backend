package com.ghtk.ecommercewebsite.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String country;
    private String province;
    private String district;
    private String detail;
    private String phone;
    private String name;

    // Getters and setters
}
