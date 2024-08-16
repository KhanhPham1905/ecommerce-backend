package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutDirectRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutRequestDTO;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.checkout.ICheckoutService;
import com.ghtk.ecommercewebsite.services.payment.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final ICheckoutService checkoutService;
    private final PaymentService paymentService;

    @PostMapping("/checkout_cart")
    public ResponseEntity<CommonResult<Map<String, String>>> checkout(@RequestBody CheckoutRequestDTO checkoutRequest) {
        try {
            OrdersDTO ordersDTO = checkoutService.checkoutCart(
                    checkoutRequest.getUserId(),
                    checkoutRequest.isMethod(),
                    checkoutRequest.getAddressID(),
                    checkoutRequest.getNote()
            );

            // Gọi phương thức tạo phiên thanh toán sau khi tạo đơn hàng thành công
            Map<String, Object> paymentSession = paymentService.createCheckoutSession(ordersDTO.getId());

            // Lấy URL từ phiên thanh toán
            String checkoutUrl = (String) paymentSession.get("url");

            // Trả về URL cho client để thực hiện chuyển hướng
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("url", checkoutUrl);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommonResult.success(responseBody, "Checkout successful, please proceed to payment"));
        } catch (IllegalArgumentException | StripeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @PostMapping("/checkout_direct")
    public ResponseEntity<CommonResult<Map<String, Object>>> checkoutDirect(@Valid @RequestBody CheckoutDirectRequestDTO checkoutDirectRequest) {
        try {
            OrdersDTO ordersDTO = checkoutService.checkoutDirect(
                    checkoutDirectRequest.getUserId(),
                    checkoutDirectRequest.getAddressID(),
                    checkoutDirectRequest.getProductItemId(),
                    checkoutDirectRequest.getQuantity(),
                    checkoutDirectRequest.getVoucherId(),
                    checkoutDirectRequest.getNote(),
                    checkoutDirectRequest.isMethod()
            );

            // Gọi phương thức tạo phiên thanh toán sau khi tạo đơn hàng thành công
            Map<String, Object> paymentSession = paymentService.createCheckoutSession(ordersDTO.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommonResult.success(paymentSession, "Checkout direct successful, please proceed to payment"));
        } catch (IllegalArgumentException | StripeException e) {
            return ResponseEntity.badRequest()
                    .body(CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
