//package com.ghtk.ecommercewebsite.services;
//
//import com.ghtk.ecommercewebsite.models.requests.EmailRequest;
//import com.ghtk.ecommercewebsite.models.requests.SendEmailRequest;
//import com.ghtk.ecommercewebsite.models.requests.Sender;
//import com.ghtk.ecommercewebsite.models.responses.EmailResponse;
//import com.ghtk.ecommercewebsite.repositories.httpclient.EmailClient;
//import feign.FeignException;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//public class BrevoEmailService {
//    EmailClient emailClient;
//
//    public EmailResponse sendEmail(SendEmailRequest request) {
//        EmailRequest emailRequest = EmailRequest.builder()
//                .sender(Sender.builder()
//                        .name("Ecommerce Website")
//                        .email("quangtruongpc88@gmail.com")
//                        .build())
//                .to(List.of(request.getTo()))
//                .subject(request.getSubject())
//                .htmlContent(request.getHtmlContent())
//                .build();
//
//        try {
//            return emailClient.sendEmail(apiKey, emailRequest);
//        } catch (FeignException e) {
//            throw new RuntimeException("User existed");
//        }
//    }
//}
