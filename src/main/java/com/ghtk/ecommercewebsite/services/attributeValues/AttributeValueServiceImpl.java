package com.ghtk.ecommercewebsite.services.attributeValues;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.AttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.entities.AttributeValues;
import com.ghtk.ecommercewebsite.repositories.AttributeValuesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements AttributeValueService{
    private  final AttributeValuesRepository attributeValuesRepository;

    @Override
    @Transactional
    public void createAttributeValues(AttributeValuesDTO attributeValuesDTO, Long userId) throws Exception {
        AttributeValues attributeValues = AttributeValues.builder()
                .attributeId(attributeValuesDTO.getAttributeId())
                .value(attributeValuesDTO.getValue())
                .isDelete(Boolean.FALSE)
                .build();
        attributeValuesRepository.save(attributeValues);
    }

    @Override
    @Transactional
    public void deleteAttributeValues(Long id, Long userid) throws Exception {
        AttributeValues attributeValues = attributeValuesRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find attribute values by id"));

        attributeValuesRepository.softDeleteById(id);

    }

    @Override
    public List<AttributeValues> getALLAttributeValues(Long id, Long userId) throws Exception {
        List<AttributeValues> attributeValuesList = attributeValuesRepository.findAttributeValuesByAttributeId(id);
        return attributeValuesList;
    }
}
