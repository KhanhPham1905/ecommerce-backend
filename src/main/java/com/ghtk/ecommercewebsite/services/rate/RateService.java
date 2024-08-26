package com.ghtk.ecommercewebsite.services.rate;

import java.math.BigDecimal;

public interface RateService {

    void updateRate(Long productId);

    BigDecimal getAverageStarsByProductId(Long productId) ;
}
