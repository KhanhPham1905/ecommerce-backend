package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductItemAttributesDTO {

    @NotBlank(message = "Value cannot be blank")
    @JsonProperty("value")
    private String value;

    @Min(value=1, message =  "Product attributes ID is required")
    @JsonProperty("product_attributes_id")
    private Long productAttributesId;

    private Long id;

    public ProductItemAttributesDTO(String value, Long productAttributesId) {
    }
//    @Min(value=1, message =  "Product item ID is required")
//    @JsonProperty("product_item_id")
//    private Long productItemId;
}
