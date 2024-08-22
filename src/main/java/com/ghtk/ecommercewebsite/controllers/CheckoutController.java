package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutDirectRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutRequestDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.checkout.ICheckoutService;
import com.ghtk.ecommercewebsite.services.payment.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
            // Lấy thông tin người dùng từ SecurityContextHolder
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = user.getId(); // Lấy userId từ đối tượng User
            // Thực hiện checkout
            OrdersDTO ordersDTO = checkoutService.checkoutCart(
                    userId, // Thay thế checkoutRequest.getUserId() bằng userId
                    checkoutRequest.isMethod(),
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

//    @PostMapping("/checkout_direct")
//    public ResponseEntity<CommonResult<Map<String, Object>>> checkoutDirect(@Valid @RequestBody CheckoutDirectRequestDTO checkoutDirectRequest) {
//        try {
//            // Lấy thông tin người dùng từ SecurityContextHolder
//            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            Long userId = user.getId(); // Lấy userId từ đối tượng User
//
//            // Thực hiện checkout trực tiếp
//            OrdersDTO ordersDTO = checkoutService.checkoutDirect(
//                    userId, // Thay thế checkoutDirectRequest.getUserId() bằng userId
//                    checkoutDirectRequest.getAddressID(),
//                    checkoutDirectRequest.getProductItemId(),
//                    checkoutDirectRequest.getQuantity(),
//                    checkoutDirectRequest.getVoucherId(),
//                    checkoutDirectRequest.getNote(),
//                    checkoutDirectRequest.isMethod()
//            );
//
//            // Gọi phương thức tạo phiên thanh toán sau khi tạo đơn hàng thành công
//            Map<String, Object> paymentSession = paymentService.createCheckoutSession(ordersDTO.getId());
//
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(CommonResult.success(paymentSession, "Checkout direct successful, please proceed to payment"));
//        } catch (IllegalArgumentException | StripeException e) {
//            return ResponseEntity.badRequest()
//                    .body(CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
//        }
//    }
}