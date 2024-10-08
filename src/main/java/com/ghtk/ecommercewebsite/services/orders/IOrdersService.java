package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.OrderItem;
import com.ghtk.ecommercewebsite.models.entities.OrderStatusHistory;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.models.entities.User;

import java.util.List;
import java.util.Optional;

public interface IOrdersService {
    OrdersDTO addOrder(OrdersDTO orderDTO, Long userId);

    List<Orders> findAll(Long userId);

    Optional<Orders> findById(Long id);

    Orders save(Orders order);

    void deleteById(Long id);

    List<Orders> findByUserId(Long userId); // Thêm phương thức này

    void updateOrderStatus(Long orderId, Orders.OrderStatus newStatus);

    List<OrderItem> getOrderItems(Long orderId);

    List<Orders> getAllOrderBySeller(Long userId) ;

    List<OrderStatusHistory> getOrderHistory(Long orderId);

    boolean checkUserPurchasedProduct(Long userId, Long productId);
}
