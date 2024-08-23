package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.mapper.OrderMapper;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.repositories.*;
import com.ghtk.ecommercewebsite.services.address.AddressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements ICheckoutService {

    private final CartItemRepository cartItemRepository;
    private final OrdersRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final VoucherRepository voucherRepository;
    private final ProductItemRepository productItemRepository;
    private final OrderMapper orderMapper;

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;
    private final AddressService addressService;


    @Transactional
    @Override
    public OrdersDTO checkoutCart(Long userId, boolean method, String note, List<Long> selectedCartItems ) {
        validateCheckoutRequest(userId);

        // Lấy danh sách các CartItem được chọn
        List<CartItem> cartItemList = cartItemRepository.findByUserIdAndIdIn(userId, selectedCartItems);
        if (cartItemList.isEmpty()) throw new IllegalArgumentException("Không có sản phẩm nào được chọn để thanh toán");


        Orders orders = createOrder(userId, method, note);

        Map<Long, ProductItem> productItemMap = fetchProductItems(cartItemList);
        Map<Long, Voucher> voucherMap = fetchVouchers(cartItemList);
        BigDecimal totalPrice = processCartItems(cartItemList, orders, productItemMap, voucherMap);
        orders.setTotalPrice(totalPrice);

        orderRepository.save(orders);
        cartItemRepository.deleteAllByIdIn(selectedCartItems);

        return orderMapper.toDto(orders);
    }

    private void validateCheckoutRequest(Long userId) throws IllegalArgumentException {
        // Kiểm tra userId có tồn tại trong Address hay không
        boolean exists = addressService.isUserInAddress(userId);
        // Nếu không tồn tại, ném ngoại lệ với thông báo phù hợp
        if (!exists) {
            throw new IllegalArgumentException("User with id " + userId + " does not have a valid address");
        }
    }


    private Orders createOrder(Long userId, boolean method, String note) {
        // Tạo đối tượng đơn hàng nhưng chưa lưu vào DB
        Orders orders = Orders.builder()
                .status(Orders.OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .note(note)
                .totalPrice(BigDecimal.ZERO)
                .method(method)
                .userId(userId)
                .build();

        // Lưu đơn hàng vào cơ sở dữ liệu và đảm bảo order đã có ID
        orders = orderRepository.save(orders);

        // Ghi lại lịch sử trạng thái đơn hàng
        saveOrderStatusHistory(orders, orders.getStatus());

        return orders;
    }


    private void saveOrderStatusHistory(Orders order, Orders.OrderStatus status) {
        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(order.getId())
                .status(status)
                .changedAt(LocalDateTime.now())
                .build();
        orderStatusHistoryRepository.save(history);
    }


    private Map<Long, ProductItem> fetchProductItems(List<CartItem> cartItemList) {
        List<Long> productItemIds = cartItemList.stream()
                .map(CartItem::getProductItemId)
                .collect(Collectors.toList());
        return productItemRepository.findAllById(productItemIds)
                .stream()
                .collect(Collectors.toMap(ProductItem::getId, item -> item));
    }

    private Map<Long, Voucher> fetchVouchers(List<CartItem> cartItemList) {
        List<Long> voucherIds = cartItemList.stream()
                .map(CartItem::getVoucherId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return voucherRepository.findAllById(voucherIds)
                .stream()
                .collect(Collectors.toMap(Voucher::getId, voucher -> voucher));
    }

    private BigDecimal processCartItems(List<CartItem> cartItemList,
                                        Orders orders,
                                        Map<Long, ProductItem> productItemMap,
                                        Map<Long, Voucher> voucherMap) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem cartItem : cartItemList) {
            ProductItem productItem = productItemMap.get(cartItem.getProductItemId());
            BigDecimal unitPrice = productItem.getPrice();
            BigDecimal discount = calculateDiscount(cartItem, productItem, voucherMap);

            BigDecimal finalPrice = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalPrice = totalPrice.add(finalPrice);

            saveOrderItem(orders, cartItem, unitPrice);

            updateProductStock(productItem, cartItem.getQuantity());
        }
        return totalPrice;
    }

    private BigDecimal calculateDiscount(CartItem cartItem, ProductItem productItem, Map<Long, Voucher> voucherMap) {
        BigDecimal discount = BigDecimal.ZERO;
        if (cartItem.getVoucherId() != null) {
            Voucher voucher = voucherMap.get(cartItem.getVoucherId());
            if (isVoucherApplicable(voucher, cartItem.getQuantity())) {
                discount = applyVoucher(voucher, productItem.getPrice(), cartItem.getQuantity());
            }
        }
        return discount;
    }

    private void saveOrderItem(Orders orders, CartItem cartItem, BigDecimal unitPrice) {
        OrderItem orderItem = OrderItem.builder()
                .orderId(orders.getId())
                .productItemId(cartItem.getProductItemId())
                .quantity(cartItem.getQuantity())
                .unitPrice(unitPrice)
                .voucherId(cartItem.getVoucherId())
                .createdAt(LocalDateTime.now())
                .build();
        orderItemRepository.save(orderItem);
    }

    private void updateProductStock(ProductItem productItem, int quantity) {
        if (productItem.getQuantity() < quantity) throw new IllegalArgumentException("Số lượng sản phẩm không đủ");
        productItem.setQuantity(productItem.getQuantity() - quantity);
        productItemRepository.save(productItem);
    }

    @Override
    public OrdersDTO checkoutDirect(Long userId,
                                    Long productItemId,
                                    int quantity,
                                    Long voucherId,
                                    String note,
                                    boolean method) {
        validateDirectCheckout(productItemId, quantity);

        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));

        if (productItem.getQuantity() < quantity)
            throw new IllegalArgumentException("Insufficient stock for the product item");

        Orders orders = createOrder(userId, method, note);

        BigDecimal unitPrice = productItem.getPrice();
        BigDecimal discount = calculateDirectDiscount(voucherId, quantity, unitPrice);

        BigDecimal finalPrice = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));
        saveDirectOrderItem(orders, productItemId, quantity, unitPrice, voucherId);

        orders.setTotalPrice(finalPrice);
        orderRepository.save(orders);
        updateProductStock(productItem, quantity);

        return orderMapper.toDto(orders);
    }

    private void validateDirectCheckout(Long productItemId, int quantity) {
        if (productItemId == null || quantity <= 0)
            throw new IllegalArgumentException("Invalid product item or quantity");
    }

    private BigDecimal calculateDirectDiscount(Long voucherId, int quantity, BigDecimal unitPrice) {
        BigDecimal discount = BigDecimal.ZERO;
        if (voucherId != null) {
            Voucher voucher = voucherRepository.findById(voucherId)
                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));
            if (isVoucherApplicable(voucher, quantity)) {
                discount = applyVoucher(voucher, unitPrice, quantity);
            }
        }
        return discount;
    }



    private void saveDirectOrderItem(Orders orders, Long productItemId, int quantity, BigDecimal unitPrice, Long voucherId) {
        OrderItem orderItem = OrderItem.builder()
                .orderId(orders.getId())
                .productItemId(productItemId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .voucherId(voucherId)
                .createdAt(LocalDateTime.now())
                .build();
        orderItemRepository.save(orderItem);
    }

    private boolean isVoucherApplicable(Voucher voucher, int quantity) {
        return voucher.isActive() &&
                LocalDateTime.now().isBefore(voucher.getExpiredAt()) &&
                quantity >= voucher.getMinimumQuantityNeeded();
    }

    private BigDecimal applyVoucher(Voucher voucher, BigDecimal unitPrice, int quantity) {
        BigDecimal discount = voucher.getDiscountType() == 1
                ? unitPrice.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100))
                : voucher.getDiscountValue();
        return discount.min(voucher.getMaximumDiscountValue()).multiply(BigDecimal.valueOf(quantity));
    }
}
