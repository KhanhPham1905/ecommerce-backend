package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.OrderMapper;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.orders.IOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderMapper orderMapper;
    private final IOrdersService iOrdersService;


    @GetMapping
    public CommonResult<List<OrdersDTO>> getAllOrders() {
        List<OrdersDTO> orders = iOrdersService.findAll().stream().map(orderMapper::toDto).collect(Collectors.toList());
        return CommonResult.success(orders, "Get all orders successfully");
    }

    @GetMapping("/{id}")
    public CommonResult<OrdersDTO> getOrderById(@PathVariable Long id) {
        return iOrdersService.findById(id)
                .map(order -> CommonResult.success(orderMapper.toDto(order), "Get order successfully"))
                .orElse(CommonResult.error(404, "Order not found"));
    }

    @GetMapping("/user/{userId}")
    public CommonResult<List<OrdersDTO>> getUserOrders(@PathVariable Long userId) {
        List<Orders> ordersList = iOrdersService.findByUserId(userId);
        List<OrdersDTO> ordersDTOList = ordersList.stream().map(orderMapper::toDto).collect(Collectors.toList());
        return CommonResult.success(ordersDTOList, "Get user orders successfully");
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public CommonResult<OrdersDTO> createOrder(@RequestBody OrdersDTO ordersDTO) throws DataNotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return CommonResult.success(iOrdersService.addOrder(ordersDTO, user.getId()));
//        Orders order = orderMapper.toEntity(ordersDTO);
//        Orders savedOrder = iOrdersService.save(order);
//        return CommonResult.success(orderMapper.toDto(savedOrder), "Create order successfully");
    }


    @PutMapping("/{id}")
    public CommonResult<OrdersDTO> updateOrder(@PathVariable Long id, @RequestBody OrdersDTO orderDetails) {
        return iOrdersService.findById(id)
                .map(order -> {
                    order.setNote(orderDetails.getNote());
                    order.setStatus(Orders.OrderStatus.valueOf(orderDetails.getStatus().toString()));
                    order.setTotalPrice(orderDetails.getTotalPrice());
                    order.setMethod(orderDetails.isMethod());
                    Orders updatedOrder = iOrdersService.save(order);
                    return CommonResult.success(orderMapper.toDto(updatedOrder), "Update order successfully");
                }).orElse(CommonResult.error(404, "Order not found"));
    }



    @PatchMapping("/{id}")
    public CommonResult<OrdersDTO> patchOrder(@PathVariable Long id, @RequestBody OrdersDTO orderDetails) {
        return iOrdersService.findById(id)
                .map(order -> {
                    if (orderDetails.getNote() != null) order.setNote(orderDetails.getNote());
                    if (orderDetails.getStatus() != null)
                        order.setStatus(Orders.OrderStatus.valueOf(orderDetails.getStatus().toString()));
                    if (orderDetails.getTotalPrice() != null) order.setTotalPrice(orderDetails.getTotalPrice());
                    if (orderDetails.isMethod()) order.setMethod(orderDetails.isMethod());
                    Orders updatedOrder = iOrdersService.save(order);
                    return CommonResult.success(orderMapper.toDto(updatedOrder), "Patch order successfully");
                }).orElse(CommonResult.error(404, "Order not found"));
    }


    @DeleteMapping("/{id}")
    public CommonResult<String> deleteOrder(@PathVariable Long id) {
        return iOrdersService.findById(id)
                .map(order -> {
                    iOrdersService.deleteById(id);
                    return CommonResult.success("Order with ID " + id + " has been deleted.");
                })
                .orElse(CommonResult.error(404, "Order not found"));
    }


//    @GetMapping("/seller")
//    @PreAuthorize("hasRole('ROLE_SELLER')")
//    public CommonResult<List<Orders>> getAllOrderBySeller()
//    throws Exception{
//        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Orders> ordersList = iOrdersService.getAllOrderBySeller(user.getId());
//        return CommonResult.success(ordersList);
//    }

}
