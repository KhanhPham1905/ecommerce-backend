package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;

import java.math.BigDecimal;
import java.util.List;


public interface ICheckoutService {

    List<OrdersDTO> checkoutCart(Long userId, boolean method, String note, List<Long> selectedCartItems);

    BigDecimal calculateCartTotal(Long userId, List<Long> selectedCartItems);

}