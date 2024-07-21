package com.ghtk.ecommercewebsite.model.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "brand")
public class Brand {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;
 private String thumbnail;

 // Getters and setters
}