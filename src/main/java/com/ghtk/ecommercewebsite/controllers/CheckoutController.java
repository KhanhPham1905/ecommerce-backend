package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.OrdersDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutRequestDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.CheckoutDirectRequestDTO;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.checkout.ICheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final ICheckoutService checkoutService;


    @PostMapping("/checkout_cart")
    public ResponseEntity<CommonResult<OrdersDTO>> checkout(@RequestBody CheckoutRequestDTO checkoutRequest) {
        try {
            OrdersDTO ordersDTO = checkoutService.checkoutCart(
                    checkoutRequest.getUserId(),
                    checkoutRequest.isMethod(),
                    checkoutRequest.getAddressID(),
                    checkoutRequest.getNote()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommonResult.success(ordersDTO, "Checkout successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    /**
     * Thực hiện checkout trực tiếp cho một sản phẩm.
     * @param checkoutDirectRequest DTO chứa thông tin yêu cầu checkout trực tiếp.
     * @return Đơn hàng đã được tạo.
     */
    @PostMapping("/checkout_direct")
    public ResponseEntity<CommonResult<OrdersDTO>> checkoutDirect(@Valid @RequestBody CheckoutDirectRequestDTO checkoutDirectRequest) {
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
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CommonResult.success(ordersDTO, "Checkout direct successful"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(CommonResult.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }
}
