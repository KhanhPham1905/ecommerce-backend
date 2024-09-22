package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.models.dtos.AttributeValuesDTO;
import com.ghtk.ecommercewebsite.models.entities.AttributeValues;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.attributeValues.AttributeValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attribute-values")
public class AttributeValuesController {
    private final AttributeValueService attributeValueService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<String> createAttributeValues(
            @Valid @RequestBody AttributeValuesDTO attributeValuesDTO
    ){
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        attributeValueService.createAttributeValues(attributeValuesDTO, user.getId());
        return CommonResult.success("Create attribute values successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<String> deleteAttributeValues(
            @PathVariable Long id
    ){
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        attributeValueService.deleteAttributeValues(id, user.getId());
        return CommonResult.success("Delete attribute values successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<List<AttributeValues>> getALLAttributeValues(
            @PathVariable Long id
    ){
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        attributeValueService.deleteAttributeValues(id, user.getId());
        return CommonResult.success(attributeValueService.getALLAttributeValues(id, user.getId()),"Delete attribute values successfully");
    }

}
