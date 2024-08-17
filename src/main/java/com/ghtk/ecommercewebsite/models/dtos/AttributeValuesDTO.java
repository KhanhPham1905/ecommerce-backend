package com.ghtk.ecommercewebsite.models.dtos;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttributeValuesDTO {
    @JsonProperty("attribute_id")
    private Long attributeId;

    private String value;
}
