//package com.ghtk.ecommercewebsite.utils;
//
//import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
//import com.ghtk.ecommercewebsite.models.entities.Orders;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrdersConverter {
//
////    @Bean
//    public static OrdersDTO convertToDto(Orders order) {
//        if (order == null) {
//            return null;
//        }
//
//        return OrdersDTO.builder()
//                .id(order.getId())
//                .createdAt(order.getCreatedAt())
//                .modifiedAt(order.getModifiedAt())
//                .note(order.getNote())
//                .totalPrice(order.getTotalPrice())
//                .receiverName(order.getId())
//                .build();
//    }
//
////    @Bean
//    public static Orders convertToEntity(OrdersDTO orderDto) {
//        if (orderDto == null) {
//            return null;
//        }
//
//        return Orders.builder()
//                .id(orderDto.getId())
//                .createdAt(orderDto.getCreatedAt())
//                .modifiedAt(orderDto.getModifiedAt())
//                .note(orderDto.getNote())
//                .price(orderDto.getPrice())
//                .receiverName(orderDto.getReceiverName())
//                .receiverPhone(orderDto.getReceiverPhone())
//                .status(Orders.OrderStatus.valueOf(orderDto.getStatus().name()))
//                .totalPrice(orderDto.getTotalPrice())
//                .voucher(orderDto.getVoucherId())
//                .buyer(orderDto.getBuyer())
//                .createdBy(orderDto.getCreatedBy())
//                .modifiedBy(orderDto.getModifiedBy())
//                .shopId(orderDto.getShopId())
//                .method(orderDto.isMethod())
//                .address(orderDto.getAddress())
//                .addressDetail(orderDto.getAddressDetail())
//                .build();
//    }
//
////    @Bean
//    public static void updateEntityFromDto(Orders order, OrdersDTO orderDto) {
//        if (orderDto == null || order == null) {
//            return;
//        }
//
//        order.setNote(orderDto.getNote());
//        order.setPrice(orderDto.getPrice());
//        order.setReceiverName(orderDto.getReceiverName());
//        order.setReceiverPhone(orderDto.getReceiverPhone());
//        order.setStatus(Orders.OrderStatus.valueOf(orderDto.getStatus().name()));
//        order.setTotalPrice(orderDto.getTotalPrice());
//        order.setVoucher(orderDto.getVoucherId());
//        order.setBuyer(orderDto.getBuyer());
//        order.setCreatedBy(orderDto.getCreatedBy());
//        order.setModifiedBy(orderDto.getModifiedBy());
//        order.setShopId(orderDto.getShopId());
//        order.setMethod(orderDto.isMethod());
//        order.setAddress(orderDto.getAddress());
//        order.setAddressDetail(orderDto.getAddressDetail());
//    }
//
//    public OrdersDTO convertToDtoInstance(Orders order) {
//        if (order == null) {
//            return null;
//        }
//
//        return OrdersDTO.builder()
//                .id(order.getId())
//                .createdAt(order.getCreatedAt())
//                .modifiedAt(order.getModifiedAt())
//                .note(order.getNote())
//                .price(order.getPrice())
//                .receiverName(order.getReceiverName())
//                .receiverPhone(order.getReceiverPhone())
//                .status(OrdersDTO.OrderStatus.valueOf(order.getStatus().name()))
//                .totalPrice(order.getTotalPrice())
//                .voucherId(order.getVoucher())
//                .buyer(order.getBuyer())
//                .createdBy(order.getCreatedBy())
//                .modifiedBy(order.getModifiedBy())
//                .shopId(order.getShopId())
//                .method(order.isMethod())
//                .address(order.getAddress())
//                .addressDetail(order.getAddressDetail())
//                .build();
//    }
//}
