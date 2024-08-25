package com.ghtk.ecommercewebsite.services.payment;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.entities.OrderItem;
import com.ghtk.ecommercewebsite.models.entities.Orders;
import com.ghtk.ecommercewebsite.models.entities.Product;
import com.ghtk.ecommercewebsite.models.entities.ProductItem;
import com.ghtk.ecommercewebsite.repositories.OrdersRepository;
import com.ghtk.ecommercewebsite.repositories.ProductItemRepository;
import com.ghtk.ecommercewebsite.repositories.ProductRepository;
import com.ghtk.ecommercewebsite.services.orders.IOrdersService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrdersRepository orderRepository;
    private final IOrdersService iOrdersService;
    private final ProductItemRepository productItemRepository;
    private final ProductRepository productRepository;


    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Map<String, Object> createCheckoutSession(List<OrdersDTO> ordersDTOList) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        // Tạo danh sách LineItems từ thông tin của tất cả các đơn hàng
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();

        for (OrdersDTO ordersDTO : ordersDTOList) {
            Long orderId = ordersDTO.getId();  // Lấy orderId từ OrdersDTO

            // Lấy thông tin đơn hàng từ cơ sở dữ liệu
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Lấy danh sách các OrderItem cho đơn hàng hiện tại
            List<OrderItem> orderItems = iOrdersService.getOrderItems(orderId);

            for (OrderItem item : orderItems) {
                // Lấy ProductItem dựa trên productItemId
                Optional<ProductItem> productItemOptional = productItemRepository.findById(item.getProductItemId());
                if (productItemOptional.isPresent()) {
                    ProductItem productItem = productItemOptional.get();

                    // Lấy Product dựa trên productId trong ProductItem
                    Optional<Product> productOptional = productRepository.findById(productItem.getProductId());
                    if (productOptional.isPresent()) {
                        Product product = productOptional.get();
                        String productName = product.getName();  // Lấy tên sản phẩm
                        // Tạo PriceData và ProductData cho phiên thanh toán Stripe
                        SessionCreateParams.LineItem.PriceData.ProductData productData =
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Product Name: " + productName)  // Sử dụng tên sản phẩm thực tế
                                        .build();

                        SessionCreateParams.LineItem.PriceData priceData =
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("vnd")
                                        .setUnitAmount(item.getUnitPrice().multiply(new BigDecimal(1000)).longValue())  // Đơn giá sản phẩm
                                        .setProductData(productData)
                                        .build();

                        SessionCreateParams.LineItem lineItem =
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(priceData)
                                        .setQuantity((long) item.getQuantity())
                                        .build();

                        lineItems.add(lineItem);
                    }
                }
            }
        }

        // Tạo phiên thanh toán Stripe
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addAllLineItem(lineItems)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .putMetadata("order_ids", ordersDTOList.stream()
                                .map(order -> String.valueOf(order.getId()))
                                .reduce((a, b) -> a + "," + b)
                                .orElse(""))
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