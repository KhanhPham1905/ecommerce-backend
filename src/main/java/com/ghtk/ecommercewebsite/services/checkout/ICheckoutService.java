package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;

import java.util.List;


public interface ICheckoutService {

    OrdersDTO checkoutCart(Long userId, boolean method, String note, List<Long> selectedCartItems);

    OrdersDTO checkoutDirect(Long userId,
                             Long productItemId,
                             int quantity,
                             Long voucherId,
                             String note,
                             boolean method);
}
