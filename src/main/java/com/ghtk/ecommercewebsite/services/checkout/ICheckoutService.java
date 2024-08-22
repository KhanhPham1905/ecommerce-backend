package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;


public interface ICheckoutService {

    OrdersDTO checkoutCart(Long userId, boolean method, String note);

    OrdersDTO checkoutDirect(Long userId,
                             Long productItemId,
                             int quantity,
                             Long voucherId,
                             String note,
                             boolean method);
}
