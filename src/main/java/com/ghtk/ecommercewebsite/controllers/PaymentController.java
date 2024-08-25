package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.services.payment.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession(@RequestBody List<OrdersDTO> ordersDTOList) throws StripeException {
        return paymentService.createCheckoutSession(ordersDTOList);
    }


    @GetMapping("/checkout/success")
    public Map<String, Object> success(@RequestParam("session_id") String sessionId) {
        // Xử lý sau khi thanh toán thành công
        Map<String, Object> response = Map.of(
                "status", "success",
                "session_id", sessionId,
                "message", "Payment was successful!"
        );
        return response;
    }

    @GetMapping("/checkout/cancel")
    public Map<String, Object> cancel() {
        // Xử lý khi thanh toán bị hủy
        Map<String, Object> response = Map.of(
                "status", "cancel",
                "message", "Payment was cancelled!"
        );
        return response;
    }
}
