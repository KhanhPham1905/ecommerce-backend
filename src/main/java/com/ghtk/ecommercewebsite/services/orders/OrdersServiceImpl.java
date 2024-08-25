package com.ghtk.ecommercewebsite.services.orders;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.mapper.OrderMapper;
import com.ghtk.ecommercewebsite.models.dtos.CartItemDTO;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements IOrdersService {

    private final OrderMapper orderMapper;
    private final OrdersRepository ordersRepository;
    private final UserRepository userRepository;
    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShopRepository shopRepository;
    private final AddressRepository addressRepository;

    @Override
    public OrdersDTO addOrder(OrdersDTO orderDTO, User user) throws DataNotFoundException {
        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new DataNotFoundException("Cannot find user by this id");
        }
        Address address = addressRepository.findByUserId(user.getId())
                .orElseThrow(()-> new DataNotFoundException("Cannot find address by userId"));
        String addressReceiver = address.getCommune() + ", " + address.getDistrict() + ", " + address.getProvince() + "," + address.getCountry();
        Orders order = orderMapper.toEntity(orderDTO);
//        order.setAddress(addressReceiver);
//        order.setAddressDetail(address.getAddressDetail());
//        order.setBuyer(user.getFullName());
//        order.setReceiverPhone(user.getPhone());
//        order.setShopId();
        ordersRepository.save(order);
        return orderDTO;
    }

    @Override
    public List<Orders> findAll(Long userId) throws DataNotFoundException {
        Shop shop = shopRepository.findByUserId(userId);
        if(shop == null) {
            throw new DataNotFoundException("Cannot find shop by userId");
        }

        return ordersRepository.findAllByShopId(shop.getId());
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

    @Override
    public void updateOrderStatus(Long orderId, Orders.OrderStatus newStatus) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Cập nhật trạng thái đơn hàng
        order.setStatus(newStatus);
        order.setModifiedAt(LocalDateTime.now());
        ordersRepository.save(order);
        // Lưu vào lịch sử thay đổi trạng thái
        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(orderId)
                .status(newStatus)
                .build();
        orderStatusHistoryRepository.save(history);
    }

}
