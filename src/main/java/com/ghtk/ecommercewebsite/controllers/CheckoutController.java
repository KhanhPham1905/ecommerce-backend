package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutRequestDTO;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.checkout.ICheckoutService;
import com.ghtk.ecommercewebsite.services.payment.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final ICheckoutService checkoutService;
    private final PaymentService paymentService;

    @PostMapping("/checkout_cart")
    public ResponseEntity<CommonResult<Map<String, String>>> checkout(@Valid  @RequestBody CheckoutRequestDTO checkoutRequest){
        try {
            // Lấy thông tin người dùng từ SecurityContextHolder
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long userId = user.getId(); // Lấy userId từ đối tượng User
            // Thực hiện checkout
            List<OrdersDTO> ordersDTO = checkoutService.checkoutCart(
                    userId, // Thay thế checkoutRequest.getUserId() bằng userId
                    checkoutRequest.isMethod(),
                    checkoutRequest.getNote(),
                    checkoutRequest.getSelectedCartItems()
            );

            // Gọi phương thức tạo phiên thanh toán sau khi tạo đơn hàng thành công
            Map<String, Object> paymentSession = paymentService.createCheckoutSession(ordersDTO);

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


    @GetMapping("/total-price")
    public CommonResult<BigDecimal> getTotalPrice(
            @RequestParam List<Long> selectedCartItems
    ) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        BigDecimal totalPrice = checkoutService.calculateCartTotal(user.getId(), selectedCartItems);
        return CommonResult.success(totalPrice, "Total price calculated successfully");
    }
}