package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_item_attributes")
public class ProductItemAttributes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

}


