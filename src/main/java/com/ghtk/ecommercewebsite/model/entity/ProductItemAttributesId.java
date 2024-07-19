package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ProductItemAttributesId implements Serializable {
    @Column(name = "product_attributes", columnDefinition = "BIGINT")
    private long productAttributesId;

    @Column(name = "product_item_id", columnDefinition = "BIGINT")
    private long productItemId;
}