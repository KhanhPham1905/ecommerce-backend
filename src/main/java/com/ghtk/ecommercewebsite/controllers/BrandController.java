package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.mapper.BrandMapper;
import com.ghtk.ecommercewebsite.models.dtos.BrandDTO;
import com.ghtk.ecommercewebsite.models.entities.Brand;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.brand.IBrandService;
import com.ghtk.ecommercewebsite.services.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandMapper brandMapper;
    private final IBrandService iBrandService;
    private final IProductService iProductService;

    @Autowired
    public BrandController(BrandMapper brandMapper, IBrandService iBrandService, IProductService iProductService) {
        this.brandMapper = brandMapper;
        this.iBrandService = iBrandService;
        this.iProductService = iProductService;
    }

    @GetMapping
    public CommonResult<List<BrandDTO>> getAllBrands() {
        List<BrandDTO> brands = iBrandService.findAll().stream().map(brandMapper::toDTO).collect(Collectors.toList());
        return CommonResult.success(brands, "Get all brands successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<BrandDTO> getBrandByID(@PathVariable Long id) {
        return iBrandService.findById(id)
                .map(brand -> CommonResult.success(brandMapper.toDTO(brand), "Get brand successfully"))
                .orElse(CommonResult.error(404, "Brand not found"));
    }

    @PostMapping
    public CommonResult<BrandDTO> createBrand(@RequestBody BrandDTO brandDTO) {
        Brand brand = brandMapper.toEntity(brandDTO);
        Brand savedBrand = iBrandService.save(brand);
        return CommonResult.success(brandMapper.toDTO(savedBrand), "Create brand successfully");
    }

    @PutMapping("/{id}")
    public CommonResult<BrandDTO> updateBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        return iBrandService.findById(id)
                .map(brand -> {
                    brand.setDescription(brandDetails.getDescription());
                    brand.setName(brandDetails.getName());
                    brand.setStatus(brandDetails.isStatus());
                    brand.setShopId(brandDetails.getShopId());
                    Brand updatedBrand = iBrandService.save(brand);
                    return CommonResult.success(brandMapper.toDTO(updatedBrand), "Update brand successfully");
                }).orElse(CommonResult.error(404, "Brand not found"));
    }

    @PatchMapping("/{id}")
    public CommonResult<BrandDTO> patchBrand(@PathVariable Long id, @RequestBody BrandDTO brandDetails) {
        return iBrandService.findById(id)
                .map(brand -> {
                    if (brandDetails.getDescription() != null) brand.setDescription(brandDetails.getDescription());
                    if (brandDetails.getName() != null) brand.setName(brandDetails.getName());
                    if (brandDetails.getShopId() != null) brand.setShopId(brandDetails.getShopId());
                    Brand updatedBrand = iBrandService.save(brand);
                    return CommonResult.success(brandMapper.toDTO(updatedBrand), "Patch brand successfully");
                }).orElse(CommonResult.error(404, "Brand not found"));
    }

    @DeleteMapping("/{id}")
    public CommonResult<String> deleteBrand(@PathVariable Long id) {
        return iBrandService.findById(id)
                .map(brand -> {
                    iProductService.deleteBrandById(id);
                    iBrandService.deleteById(id);
                    return CommonResult.success("Brand with ID " + id + " has been deleted.");
                })
                .orElse(CommonResult.error(404, "Brand not found"));
    }
}
