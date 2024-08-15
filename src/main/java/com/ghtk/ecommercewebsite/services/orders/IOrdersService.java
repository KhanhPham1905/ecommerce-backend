package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;

import java.util.List;
import java.util.Optional;

public interface IOrdersService {
    OrdersDTO addOrder(OrdersDTO orderDTO, Long userId) throws DataNotFoundException;
    List<Orders> findAll();
    Optional<Orders> findById(Long id);
    Orders save(Orders order);
    void deleteById(Long id);
    List<Orders> findByUserId(Long userId); // Thêm phương thức này
}
