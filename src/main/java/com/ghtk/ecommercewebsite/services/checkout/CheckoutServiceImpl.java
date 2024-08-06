package com.ghtk.ecommercewebsite.services.checkout;

import com.ghtk.ecommercewebsite.mapper.OrderMapper;
import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.*;
import com.ghtk.ecommercewebsite.repositories.*;
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


    @Transactional
    @Override
    public OrdersDTO checkoutCart(Long userId, boolean method, Long addressID, String note) {
        validateCheckoutRequest(addressID);

        List<CartItem> cartItemList = cartItemRepository.findByUserId(userId);
        if (cartItemList.isEmpty()) throw new IllegalArgumentException("Giỏ hàng trống");

        Orders orders = createOrder(userId, method, addressID, note);

        Map<Long, ProductItem> productItemMap = fetchProductItems(cartItemList);
        Map<Long, Voucher> voucherMap = fetchVouchers(cartItemList);
        BigDecimal totalPrice = processCartItems(cartItemList, orders, productItemMap, voucherMap);
        orders.setTotalPrice(totalPrice);

        orderRepository.save(orders);

        cartItemRepository.deleteByUserId(userId);

        return orderMapper.toDto(orders);
    }

    private void validateCheckoutRequest(Long addressID) {
        if (addressID == null) throw new IllegalArgumentException("Bạn cần có thông tin nhận hàng");
    }

    private Orders createOrder(Long userId, boolean method, Long addressID, String note) {
        Orders orders = Orders.builder()
                .addressID(addressID)
                .status(Orders.OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .note(note)
                .method(method)
                .userId(userId)
                .build();
        return orderRepository.save(orders);
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
        else productItem.setQuantity(productItem.getQuantity() - quantity);
        productItemRepository.save(productItem);
    }

    @Override
    public OrdersDTO checkoutDirect(Long userId,
                                    Long addressID,
                                    Long productItemId,
                                    int quantity,
                                    Long voucherId,
                                    String note,
                                    boolean method) {
        validateDirectCheckout(addressID, productItemId, quantity);

        ProductItem productItem = productItemRepository.findById(productItemId)
                .orElseThrow(() -> new IllegalArgumentException("Product item not found"));

        if (productItem.getQuantity() < quantity) throw new IllegalArgumentException("Insufficient stock for the product item");

        Orders orders = createOrder(userId, method, addressID, note);

        BigDecimal unitPrice = productItem.getPrice();
        BigDecimal discount = calculateDirectDiscount(voucherId, quantity, unitPrice);

        BigDecimal finalPrice = unitPrice.subtract(discount).multiply(BigDecimal.valueOf(quantity));
        saveDirectOrderItem(orders, productItemId, quantity, unitPrice, voucherId);

        orders.setTotalPrice(finalPrice);
        orderRepository.save(orders);

        updateProductStock(productItem, quantity);

        return orderMapper.toDto(orders);
    }

    private void validateDirectCheckout(Long addressID, Long productItemId, int quantity) {
        if (addressID == null) throw new IllegalArgumentException("Bạn cần có thông tin nhận hàng");
        if (productItemId == null || quantity <= 0) throw new IllegalArgumentException("Invalid product item or quantity");
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
