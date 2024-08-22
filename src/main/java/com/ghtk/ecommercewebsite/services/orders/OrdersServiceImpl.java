package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.entities.CartItem;
import com.ghtk.ecommercewebsite.models.entities.OrderItem;
import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.OrderMapper;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.repositories.CartItemRepository;
import com.ghtk.ecommercewebsite.repositories.OrderItemRepository;
import com.ghtk.ecommercewebsite.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements IOrdersService {

    private final OrderMapper orderMapper;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;

    private final CartItemRepository cartItemRepository;

    private final OrderItemRepository orderItemRepository;

//    @Autowired
//    public OrdersServiceImpl(OrdersRepository ordersRepository) {
//        this.ordersRepository = ordersRepository;
//    }

    // Author: Truong
    @Override
    public OrdersDTO addOrder(OrdersDTO orderDTO, Long userId) throws DataNotFoundException {
        if (userRepository.findById(userId).isEmpty()) {
            throw new DataNotFoundException("Cannot find user by this id");
        }
        Orders order = orderMapper.toEntity(orderDTO);
        ordersRepository.save(order);
        return orderDTO;
    }

    @Override
    public List<Orders> findAll() {
        return ordersRepository.findAll();
    }

    @Override
    public Optional<Orders> findById(Long id) {
        return ordersRepository.findById(id);
    }

    @Override
    public Orders save(Orders orders) {
        return ordersRepository.save(orders);
    }

    @Override
    public void deleteById(Long id) {
        ordersRepository.deleteById(id);
    }
    @Override
    public List<Orders> findByUserId(Long userId) {
        return ordersRepository.findByUserId(userId); // Giả sử repository đã có phương thức này
    }

    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    public void addProductToCart(CartItemDTO cartItemDTO, Long userId) throws Exception {
        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng của người dùng chưa
        CartItem existingCartItem = cartItemRepository.findByProductItemIdAndUserId(cartItemDTO.getProductItemId(), userId);

        if (existingCartItem != null) {
            // Nếu sản phẩm đã có trong giỏ hàng, tăng số lượng
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            cartItemRepository.save(existingCartItem);
        } else {
            // Nếu chưa có, thêm sản phẩm mới vào giỏ hàng
            CartItem newCartItem = new CartItem();
            newCartItem.setUserId(userId);
            newCartItem.setProductItemId(cartItemDTO.getProductItemId());
            newCartItem.setQuantity(cartItemDTO.getQuantity());
            cartItemRepository.save(newCartItem);
        }
    }


    @Override
    public List<Orders> getAllOrderBySeller(Long userId) throws Exception {
        return List.of();
    }
}
