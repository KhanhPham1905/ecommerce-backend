package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.inventory.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public  CommonResult<Page<DetailInventoryDTO>> getAllInventoryById(
            @RequestParam(defaultValue = "0",required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "",required = false) String warehouse,
            @RequestParam(defaultValue = "",required = false) String skuCode,
            @RequestParam(defaultValue = "",required = false) String name
    ){
        PageRequest pageRequest = PageRequest.of(
                page, size);
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(inventoryService.getAllInventory(warehouse, skuCode, name,  user.getId(), pageRequest),"Get all inventory successfully");
    }


    @PostMapping("/import")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<DetailInventoryDTO> importWarehouse(
            @Valid @RequestBody DetailInventoryDTO detailInventoryDTO
    ){
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(inventoryService.importWarehouse(detailInventoryDTO, user.getId()), "Import Inventory successfully");
    }


    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<DetailInventoryDTO>> getListExport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "",required = false) String warehouse,
            @RequestParam(defaultValue = "",required = false) String supplier,
            @RequestParam(defaultValue = "",required = false) String location,
            @RequestParam(defaultValue = "",required = false) String skuCode,
            @RequestParam(defaultValue = "",required = false) String name,
            @RequestParam(defaultValue = "",required = false) String createdAt
    ){
        Pageable pageable = PageRequest.of(page, size);
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<DetailInventoryDTO>  listExportsPages = inventoryService.getListExport(warehouse,supplier,location,skuCode, name , createdAt,user.getId(), pageable);
        return  CommonResult.success(listExportsPages, "Get list export successfully");
    }


    @GetMapping("/import")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<Page<DetailInventoryDTO>> getListImport(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "",required = false) String warehouse,
            @RequestParam(defaultValue = "",required = false) String supplier,
            @RequestParam(defaultValue = "",required = false) String location,
            @RequestParam(defaultValue = "",required = false) String skuCode,
            @RequestParam(defaultValue = "",required = false) String name,
            @RequestParam(defaultValue = "",required = false) String createdAt
    ){
        Pageable pageable = PageRequest.of(page, size);
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<DetailInventoryDTO> listImportsPages = inventoryService.getListImport(warehouse,supplier,location,skuCode, name , createdAt, user.getId(), pageable);
        return  CommonResult.success(listImportsPages , "Get list import successfully");
    }

}
