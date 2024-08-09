package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.models.dtos.DetailInventoryDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InventoryController {
    private final InventoryService inventoryService;

//    @GetMapping("/{id}")
//    public CommonResult<InventoryDTO> getInventoryById(
//            @PathVariable("id") Long inventoryId
//    )throws Exception{
//        return CommonResult.success(inventoryService.getInventoryById(inventoryId), "Get inventory successfully");
//    }

    @GetMapping
    public  CommonResult<List<DetailInventoryDTO>> getAllInventoryById() throws Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(inventoryService.getAllInventory(user.getId()),"Get all inventory successfully");
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<DetailInventoryDTO> importWarehouse(
            @RequestBody DetailInventoryDTO detailInventoryDTO
    ) throws  Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(inventoryService.importWarehouse(detailInventoryDTO, user.getId()), "Import Inventory successfully");
    }

//    @PostMapping("/export")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
//    public CommonResult<DetailInventoryDTO> exportWarehouse(
//            @RequestBody DetailInventoryDTO detailInventoryDTO
//    ) throws Exception{
//        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return CommonResult.success(inventoryService.exportWarehouse(,userId), "Export Inventory successfully");
//    }

    @GetMapping("/export")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<List<DetailInventoryDTO>> getListExport() throws  Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(inventoryService.getListExport(user.getId()), "Get list export successfully");
    }


    @GetMapping("/import")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<List<DetailInventoryDTO>> getListImport() throws  Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return  CommonResult.success(inventoryService.getListImport(user.getId()), "Get list import successfully");
    }
}
