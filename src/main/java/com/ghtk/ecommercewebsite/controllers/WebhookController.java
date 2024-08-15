package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.entities.Payments;
import com.ghtk.ecommercewebsite.repositories.PaymentRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhook")
public class WebhookController {

    private final PaymentRepository paymentRepository;

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    // Thay thế bằng khóa bí mật endpoint Stripe thực tế của bạn
    String endpointSecret = "whsec_1861168cb9dadd12e8a8b2c2ca5ffa10acf7c31f6bf1fb379e44fc2da2422800";

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
        String payload;
        String sigHeader = request.getHeader("Stripe-Signature");
        Event event;

        try {
            payload = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            logger.info("Payload: " + payload);
            logger.info("Signature: " + sigHeader);
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            logger.info("Event type: " + event.getType());

            // Phân tích loại sự kiện và gọi phương thức xử lý phù hợp
            switch (event.getType()) {
                case "checkout.session.completed":
                    handleCheckoutSessionCompleted(event);
                    break;
                case "payment_intent.succeeded":
                    handlePaymentIntentSucceeded(event);
                    break;
                case "charge.succeeded":
                    handleChargeSucceeded(event);
                    break;
                case "payment_intent.created":
                    handlePaymentIntentCreated(event);
                    break;
                default:
                    logger.warn("Unhandled event type: " + event.getType());
                    break;
            }

            return ResponseEntity.status(HttpStatus.OK).body("Event received");

        } catch (SignatureVerificationException e) {
            logger.error("Invalid signature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            logger.error("Event not processed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event not processed");
        }
    }

    private void handleCheckoutSessionCompleted(Event event) throws StripeException {
        Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow(() -> new RuntimeException("Failed to deserialize session object"));

        try {
            String paymentIntentId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            String orderId = session.getMetadata().get("order_id");
            if (orderId == null) {
                throw new RuntimeException("Order ID is missing in session metadata");
            }

            Payments payment = Payments.builder()
                    .orderId(Long.parseLong(orderId))
                    .amount(new BigDecimal(paymentIntent.getAmountReceived()).divide(new BigDecimal(100)))
                    .currency(paymentIntent.getCurrency())
                    .paymentStatus(paymentIntent.getStatus())
                    .paymentMethod(paymentIntent.getPaymentMethodTypes().get(0))
                    .stripeSessionId(session.getId())
                    .build();

            paymentRepository.save(payment);
            logger.info("Payment saved successfully");

        } catch (Exception e) {
            logger.error("Failed to save payment: " + e.getMessage(), e);
        }
    }

    private void handlePaymentIntentSucceeded(Event event) throws StripeException {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow(() -> new RuntimeException("Failed to deserialize payment intent object"));

        // Lấy thông tin thanh toán
        String paymentIntentId = paymentIntent.getId();
        BigDecimal amountReceived = new BigDecimal(paymentIntent.getAmountReceived()).divide(new BigDecimal(100)); // Stripe trả về số tiền tính bằng cent
        String currency = paymentIntent.getCurrency();
        String paymentStatus = paymentIntent.getStatus();
        String paymentMethod = paymentIntent.getPaymentMethodTypes().get(0); // Lấy loại phương thức thanh toán đầu tiên

        // Lưu thông tin thanh toán vào cơ sở dữ liệu hoặc xử lý theo nhu cầu
        Payments payment = new Payments();
        payment.setOrderId(Long.parseLong(paymentIntent.getMetadata().get("order_id"))); // Kiểm tra và lấy order_id từ metadata
        payment.setAmount(amountReceived);
        payment.setCurrency(currency);
        payment.setPaymentStatus(paymentStatus);
        payment.setPaymentMethod(paymentMethod);
        payment.setStripeSessionId(paymentIntent.getId());

        paymentRepository.save(payment);
        logger.info("PaymentIntent saved successfully");
    }

    private void handleChargeSucceeded(Event event) throws StripeException {
        com.stripe.model.Charge charge = (com.stripe.model.Charge) event.getDataObjectDeserializer().getObject().orElseThrow(() -> new RuntimeException("Failed to deserialize charge object"));

        // Lấy thông tin phí
        String chargeId = charge.getId();
        BigDecimal amountReceived = new BigDecimal(charge.getAmount()).divide(new BigDecimal(100)); // Stripe trả về số tiền tính bằng cent
        String currency = charge.getCurrency();
        String chargeStatus = charge.getStatus();

        // Lưu thông tin phí vào cơ sở dữ liệu hoặc xử lý theo nhu cầu
        Payments payment = new Payments();
        payment.setOrderId(Long.parseLong(charge.getMetadata().get("order_id"))); // Kiểm tra và lấy order_id từ metadata
        payment.setAmount(amountReceived);
        payment.setCurrency(currency);
        payment.setPaymentStatus(chargeStatus);
        payment.setPaymentMethod(charge.getPaymentMethod()); // Có thể lấy thông tin phương thức thanh toán từ charge
        payment.setStripeSessionId(charge.getId());

        paymentRepository.save(payment);
        logger.info("Charge saved successfully");
    }

    private void handlePaymentIntentCreated(Event event) throws StripeException {
        PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElseThrow(() -> new RuntimeException("Failed to deserialize payment intent object"));

        // Lấy thông tin `PaymentIntent` mới tạo
        String paymentIntentId = paymentIntent.getId();
        String clientSecret = paymentIntent.getClientSecret();

        // Thực hiện xử lý cần thiết khi một `PaymentIntent` mới được tạo ra
        // Ví dụ: lưu thông tin vào cơ sở dữ liệu, cập nhật trạng thái trong hệ thống, v.v.

        logger.info("PaymentIntent created: " + paymentIntentId);
    }

}
