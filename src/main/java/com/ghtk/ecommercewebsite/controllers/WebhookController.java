package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.entities.Payments;
import com.ghtk.ecommercewebsite.repositories.PaymentRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
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
                case "charge.updated":
                    handleChargeUpdated(event);
                    break;
                default:
                    logger.warn("Unhandled event type: " + event.getType());
                    break;
            }

            return ResponseEntity.status(HttpStatus.OK).body("Event received");

        } catch (Exception e) {
            logger.error("Event not processed: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Event not processed");
        }
    }

    private void handleChargeUpdated(Event event) {
        // Bạn có thể thêm logic xử lý cho sự kiện charge.updated tại đây
    }

    private void handleCheckoutSessionCompleted(Event event) {
        try {
            // Parse JSON payload into a JsonObject
            JsonObject jsonObject = JsonParser.parseString(event.getData().getObject().toString()).getAsJsonObject();

            // Verify if the JSON is indeed an object before converting
            if (jsonObject.isJsonObject()) {
                Session session = ApiResource.GSON.fromJson(jsonObject, Session.class);

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
            } else {
                throw new RuntimeException("Expected a JSON object but received a different structure");
            }

        } catch (Exception e) {
            logger.error("Failed to save payment: " + e.getMessage(), e);
        }
    }

    private void handlePaymentIntentSucceeded(Event event) {
//        try {
//            JsonElement jsonElement = JsonParser.parseString(event.getData().getObject().toString());
//
//            if (jsonElement.isJsonObject()) {
//                JsonObject jsonObject = jsonElement.getAsJsonObject();
//                PaymentIntent paymentIntent = ApiResource.GSON.fromJson(jsonObject, PaymentIntent.class);
//
//                // Tiếp tục xử lý dữ liệu
//            } else {
//                throw new RuntimeException("Expected a JSON object but received a different structure");
//            }
//
//        } catch (Exception e) {
//            logger.error("Failed to save payment: " + e.getMessage(), e);
//        }
    }

    private void handleChargeSucceeded(Event event) {
        // Bạn có thể thêm logic xử lý cho sự kiện charge.succeeded tại đây
    }

    private void handlePaymentIntentCreated(Event event) {
        // Bạn có thể thêm logic xử lý cho sự kiện payment_intent.created tại đây
    }
}
