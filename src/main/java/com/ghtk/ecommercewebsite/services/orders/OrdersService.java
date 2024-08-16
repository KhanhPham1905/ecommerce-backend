package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;

import java.util.List;
import java.util.Optional;

public interface OrdersService {
    OrdersDTO addOrder(OrdersDTO orderDTO, Long userId);
    OrdersDTO getOrderById(Long id);
    CommonResult<OrdersDTO> createOrder(OrdersDTO orderDTO);
    OrdersDTO updateOrder(Long id, OrdersDTO orderDTO);
    void deleteOrder(Long id);
    List<OrdersDTO> getAllOrders();
}
