package com.ghtk.ecommercewebsite.services.attributeValues;


import com.ghtk.ecommercewebsite.models.dtos.AttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.entities.AttributeValues;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AttributeValueService {
    void createAttributeValues(AttributeValuesDTO attributeValuesDTO, Long userId) throws  Exception;
    void deleteAttributeValues(Long id, Long userid) throws Exception;
    List<AttributeValues> getALLAttributeValues(Long id, Long userId) throws Exception;

}
