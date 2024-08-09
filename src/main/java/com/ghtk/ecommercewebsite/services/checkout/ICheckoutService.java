package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import org.springframework.web.bind.annotation.RequestParam;

public interface ICheckoutService {

    OrdersDTO checkoutCart(Long userId, boolean method, Long addressID, String note);

    OrdersDTO checkoutDirect(Long userId,
                             Long addressID,
                             Long productItemId,
                             int quantity,
                             Long voucherId,
                             String note,
                             boolean method);
}
