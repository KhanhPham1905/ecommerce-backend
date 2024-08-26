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
                .address(order.getAddress())
                .addressDetail(order.getAddressDetail())
                .buyer(order.getBuyer())
                .receiverPhone(order.getReceiverPhone())
                .id(order.getId())
                .shopId(order.getShopId())
                .userId(order.getUserId())
                .note(order.getNote())
                .status(OrdersDTO.OrderStatus.valueOf(order.getStatus().name()))
                .totalPrice(order.getTotalPrice())
                .modifiedAt(order.getModifiedAt())
                .createdAt(order.getCreatedAt())
                .method(order.isMethod())
                .build();
    }

    public Orders toEntity(OrdersDTO orderDTO) {
        return Orders.builder()
                .id(orderDTO.getId())
                .buyer(orderDTO.getBuyer())
                .addressDetail(orderDTO.getAddressDetail())
                .receiverPhone(orderDTO.getReceiverPhone())
                .address(orderDTO.getAddress())
                .shopId(orderDTO.getShopId())
                .userId(orderDTO.getUserId())
                .note(orderDTO.getNote())
                .status(Orders.OrderStatus.valueOf(orderDTO.getStatus().name()))
                .totalPrice(orderDTO.getTotalPrice())
                .method(orderDTO.isMethod())
                .build();
    }

}
