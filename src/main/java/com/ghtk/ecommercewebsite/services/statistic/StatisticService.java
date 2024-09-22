package com.ghtk.ecommercewebsite.services.statistic;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.ProductDTO;
import com.ghtk.ecommercewebsite.models.dtos.StatisticDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StatisticService {
    StatisticDTO getStatisticsOfCurrentShop(Long userId);

    StatisticDTO getStatisticsOfAnProduct(Long userId, Long productId);

    Page<ProductDTO> getAllProductsOfCurrentShop(Long userId, Pageable pageable);
}
