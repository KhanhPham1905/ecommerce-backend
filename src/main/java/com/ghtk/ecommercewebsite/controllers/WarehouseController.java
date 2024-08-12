package com.ghtk.ecommercewebsite.controllers;


import com.ghtk.ecommercewebsite.models.dtos.DetailWarehouseDTO;
import com.ghtk.ecommercewebsite.models.dtos.WarehouseDto;
import com.ghtk.ecommercewebsite.models.entities.Category;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.entities.Warehouse;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.warehouse.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping("/{id}")
    public CommonResult<DetailWarehouseDTO> getWarehouseInfo(
            @PathVariable("id") Long id
    )throws Exception{
        return CommonResult.success(warehouseService.getWarehouseInfo(id), "Get product attributes successfully");
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<DetailWarehouseDTO> createWarehouse (
            @Valid @RequestBody  DetailWarehouseDTO detailWarehouseDTO
    ) throws Exception {
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(warehouseService.createWarehouse(detailWarehouseDTO, user.getId()),"Create warehouse successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<DetailWarehouseDTO> updateWarehouseById(
            @PathVariable Long id,
            @Valid @RequestBody DetailWarehouseDTO detailWarehouseDTO
    )throws Exception{
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        warehouseService.updateWarehouseById(detailWarehouseDTO,id, user.getId());
        return CommonResult.success(warehouseService.updateWarehouseById(detailWarehouseDTO,id, user.getId()),"Update warehouse successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public  CommonResult deleteWarehouse(@PathVariable Long id) throws Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        warehouseService.deleteWarehouseById(id, user.getId());
        return CommonResult.success("Delete warehouse successfully ");
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public CommonResult<List<Warehouse>> getAllWarehouse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "",required = false) String name
    ) throws Exception {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(warehouseService.getAllWarehouse(pageRequest,user.getId(), name), "Get all categories");
    }

}
