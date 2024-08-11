package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.BrandMapper;
import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.brand.IBrandService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandMapper brandMapper;
    private final IBrandService iBrandService;

    public BrandController(BrandMapper brandMapper, IBrandService iBrandService) {
        this.brandMapper = brandMapper;
        this.iBrandService = iBrandService;
    }

    @GetMapping
    public CommonResult<List<BrandDTO>> getAllBrands() {
        List<BrandDTO> brands = iBrandService.findAllActive().stream()
                .map(brandMapper::toDTO).collect(Collectors.toList());
        return CommonResult.success(brands, "Get all brands successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<BrandDTO> getBrandByID(@PathVariable Long id) {
        return iBrandService.findActiveById(id)
                .map(brand -> CommonResult.success(brandMapper.toDTO(brand), "Get brand successfully"))
                .orElse(CommonResult.error(404, "Brand not found"));
    }

    @PostMapping
    public CommonResult<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.toEntity(brandDTO);
        Brand savedBrand = iBrandService.createBrand(brand);
        return CommonResult.success(brandMapper.toDTO(savedBrand), "Create brand successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<BrandDTO> updateBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        Brand updatedBrand = iBrandService.updateBrand(id, brandMapper.toEntity(brandDetails));
        return CommonResult.success(brandMapper.toDTO(updatedBrand), "Update brand successfully");
    }

    @PatchMapping("/{id}")
    public CommonResult<BrandDTO> patchBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        Brand updatedBrand = iBrandService.patchBrand(id, brandMapper.toEntity(brandDetails));
        return CommonResult.success(brandMapper.toDTO(updatedBrand), "Patch brand successfully");
    }

    @DeleteMapping("/{id}")
    public CommonResult<String> deleteBrand(@PathVariable Long id) {
        iBrandService.softDeleteBrand(id);
        return CommonResult.success("Brand with ID " + id + " has been deleted.");
    }
}
