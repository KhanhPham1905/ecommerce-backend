package com.ghtk.ecommercewebsite.mapper;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class OrderMapper {

    public OrdersDTO toDto(Orders order) {
        return OrdersDTO.builder()
                .id(order.getId())
                .addressID(order.getAddressID())
                .userId(order.getUserId())
                .note(order.getNote())
                .status(OrdersDTO.OrderStatus.valueOf(order.getStatus().name()))
                .totalPrice(order.getTotalPrice())
                .method(order.isMethod())
                .build();
    }

    public Orders toEntity(OrdersDTO orderDTO) {
        return Orders.builder()
                .id(orderDTO.getId())
                .addressID(orderDTO.getAddressID())
                .userId(orderDTO.getUserId())
                .note(orderDTO.getNote())
                .status(Orders.OrderStatus.valueOf(orderDTO.getStatus().name()))
                .totalPrice(orderDTO.getTotalPrice())
                .method(orderDTO.isMethod())
                .build();
    }

}
