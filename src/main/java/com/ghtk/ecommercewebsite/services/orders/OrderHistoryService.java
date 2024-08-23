package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.models.entities.OrderStatusHistory;
import com.ghtk.ecommercewebsite.repositories.OrderStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {

    @Autowired
    private OrderStatusHistoryRepository orderStatusHistoryRepository;

    public List<OrderStatusHistory> getOrderHistory(Long orderId) {
        return orderStatusHistoryRepository.findByOrderId(orderId);
    }
}
