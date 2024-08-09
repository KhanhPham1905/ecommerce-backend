//package com.ghtk.ecommercewebsite.controllers;
//
//import com.ghtk.ecommercewebsite.models.requests.SendEmailRequest;
//import com.ghtk.ecommercewebsite.models.responses.CommonResult;
//import com.ghtk.ecommercewebsite.models.responses.EmailResponse;
//import com.ghtk.ecommercewebsite.services.BrevoEmailService;
//import com.ghtk.ecommercewebsite.services.EmailService;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//// For Brevo Email Service
//@RestController
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class EmailController {
//
//    BrevoEmailService emailService;
//
//    @PostMapping("/email/send")
//    CommonResult<EmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
//        return CommonResult.success(emailService.sendEmail(request));
//    }
//}
