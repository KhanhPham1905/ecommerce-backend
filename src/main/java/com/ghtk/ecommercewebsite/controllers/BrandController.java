package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.brand.IBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
public class BrandController {
    private final IBrandService brandService;

    @GetMapping("/{id}")
    public CommonResult<Brand> getBrandById(
            @PathVariable("id") Long brandId
    )throws Exception{
        Brand existingBrand = brandService.getBrandById(brandId);
        return CommonResult.success(existingBrand, "Get brand successfully");
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CommonResult<Object> createBrand (
            @Valid @RequestBody BrandDTO brandDTO
    ) throws Exception {
        User user  = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Brand brand = brandService.createBrand(brandDTO, user.getId());
        return CommonResult.success(brand,"Create brand successfully");
    }

    @GetMapping("")
    public CommonResult<Page<Brand>> getAllBrands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "",required = false) String name
    ) throws Exception {
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("id").ascending());
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Brand> brandsPages = brandService.getAllBrands(pageRequest,user.getId(), name);
        return CommonResult.success(brandsPages, "Get all brands");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  CommonResult deleteBrand(@PathVariable Long id) throws Exception{
        User user  = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        brandService.deleteBrand(id, user.getId());
        return CommonResult.success("Delete success brand");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CommonResult<Brand> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandDTO brandDTO
    )throws Exception{
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Brand brand = brandService.updateBrand(id, brandDTO, user.getId());
        return CommonResult.success(brand, "Update brand successfully");
    }
}
