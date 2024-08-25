package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;

import java.math.BigDecimal;
import java.util.List;


public interface ICheckoutService {

    List<OrdersDTO> checkoutCart(Long userId, boolean method, String note, List<Long> selectedCartItems);

    BigDecimal calculateCartTotal(Long userId, List<Long> selectedCartItems);

//    OrdersDTO checkoutDirect(Long userId,
//                             Long productItemId,
//                             int quantity,
//                             Long voucherId,
//                             String note,
//                             boolean method);
//}
}