package com.ghtk.ecommercewebsite.models.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailProductItemDTO {
    @PositiveOrZero(message = "Price must be zero or positive")
    private BigDecimal price;

    private Integer quantity;

    private String name;

    @JsonProperty("sku_code")
    private String skuCode;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("list_product_item")
    private List<ProductItemAttributesDTO> productItemAtrAttributesDTOS;

}
