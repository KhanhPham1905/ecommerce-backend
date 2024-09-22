package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.dtos.StatisticDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.statistic.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seller/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticService statisticService;

    // An API (get overall statistics of current shop) which is not so good
    @GetMapping
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<StatisticDTO> getStatisticsOfCurrentShop() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(statisticService.getStatisticsOfCurrentShop(user.getId()));
    }

    // Get all products of current shop, so when click on one, return the
    // statistics correspondingly
    @GetMapping("/products")
    @PreAuthorize("hasRole('SELLER')")
    public CommonResult<Page<ProductDTO>> getAllProductsOfCurrentShop(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(statisticService.getAllProductsOfCurrentShop(user.getId(), pageable));
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('SELLER')")
    public CommonResult<StatisticDTO> getStatisticsOfAnProduct(@PathVariable Long productId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(statisticService.getStatisticsOfAnProduct(user.getId(), productId));
    }
}