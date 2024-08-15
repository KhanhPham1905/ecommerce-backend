package com.ghtk.ecommercewebsite.models.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;



@Getter
@Setter
@Data
public class ListAttributeValuesDTO {
    List<ProductItemAttributesDTO>  listAttributeValues;
}
