package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.models.entities.Orders;

import java.util.List;
import java.util.Optional;

public interface IOrdersService {
    List<Orders> findAll();
    Optional<Orders> findById(Long id);
    Orders save(Orders order);
    void deleteById(Long id);
}