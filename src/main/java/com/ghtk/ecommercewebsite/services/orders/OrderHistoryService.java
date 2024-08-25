package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.models.entities.OrderStatusHistory;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.repositories.OrderStatusHistoryRepository;
import com.ghtk.ecommercewebsite.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {


    private  final  OrderStatusHistoryRepository orderStatusHistoryRepository;
    private  final OrdersRepository ordersRepository;

    public List<OrderStatusHistory> getOrderHistory(Long orderId) {
        return orderStatusHistoryRepository.findByOrderId(orderId);
    }


    public void updateOrderStatus(Long orderId, Orders.OrderStatus newStatus) throws Exception {
        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found"));
        // Cập nhật trạng thái mới
        order.setStatus(newStatus);
        ordersRepository.save(order);
        // Lưu vào lịch sử trạng thái
        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(orderId)
                .status(newStatus)
                .build();
        orderStatusHistoryRepository.save(history);
    }

}
