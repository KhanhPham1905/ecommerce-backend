package com.ghtk.ecommercewebsite.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class ProductOrderId{
    @Column(name = "product_id", columnDefinition = "BIGINT")
    private long productId;

    @Column(name = "product_item_id", columnDefinition = "BIGINT")
    private long productItemId;
}