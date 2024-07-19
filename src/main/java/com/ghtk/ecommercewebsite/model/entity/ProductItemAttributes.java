package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "product_item_attributes")
public class ProductItemAttributes {

    @EmbeddedId
    private ProductItemAttributesId productItemAttributesId;

    @Column(name = "value", columnDefinition = "VARCHAR(200)")
    private String value;

}


