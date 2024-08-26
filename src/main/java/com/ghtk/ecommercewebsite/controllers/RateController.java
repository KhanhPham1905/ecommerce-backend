package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.rate.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rates")
public class RateController {

    private final RateService rateService;

    @GetMapping("/product/{productId}/average-stars")
    public CommonResult<BigDecimal> getAverageStarsByProductId(@PathVariable("productId") Long productId) {
        try {
            BigDecimal averageStars = rateService.getAverageStarsByProductId(productId);
            return CommonResult.success(averageStars, "Average stars retrieved successfully");
        } catch (Exception e) {
            return CommonResult.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An error occurred while retrieving average stars");
        }
    }
}
