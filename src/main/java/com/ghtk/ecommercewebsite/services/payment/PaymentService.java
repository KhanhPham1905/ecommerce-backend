package com.ghtk.ecommercewebsite.services.payment;

import com.ghtk.ecommercewebsite.models.entities.OrderItem;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.repositories.OrdersRepository;
import com.ghtk.ecommercewebsite.services.orders.IOrdersService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrdersRepository orderRepository;
    private final IOrdersService iOrdersService;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Map<String, Object> createCheckoutSession(Long orderId) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // Lấy thông tin đơn hàng từ cơ sở dữ liệu
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Tạo danh sách LineItems từ thông tin của đơn hàng
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        List<OrderItem> orderItems = iOrdersService.getOrderItems(orderId);
        for (OrderItem item : orderItems) {
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Product Name: " + item.getProductItemId())  // Thay bằng tên sản phẩm thực tế nếu có
                            .build();

            SessionCreateParams.LineItem.PriceData priceData =
                    SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(item.getUnitPrice().multiply(new BigDecimal(100)).longValue())  // Đơn giá sản phẩm
                            .setProductData(productData)
                            .build();

            SessionCreateParams.LineItem lineItem =
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(priceData)
                            .setQuantity((long) item.getQuantity())
                            .build();

            lineItems.add(lineItem);
        }

        // Tạo phiên thanh toán Stripe
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addAllLineItem(lineItems)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .putMetadata("order_id", String.valueOf(orderId))
                        .setSuccessUrl("http://localhost:8080/api/payment/checkout/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8080/api/payment/checkout/cancel")
                        .build();

        Session session = Session.create(params);

        // Trả về session_id và các thông tin khác
        Map<String, Object> response = new HashMap<>();
        response.put("id", session.getId());
        response.put("url", session.getUrl());

        return response;
    }
}
