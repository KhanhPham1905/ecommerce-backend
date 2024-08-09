//package com.ghtk.ecommercewebsite.repositories.httpclient;
//
//import com.ghtk.ecommercewebsite.models.requests.EmailRequest;
//import com.ghtk.ecommercewebsite.models.responses.EmailResponse;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Repository;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@Repository
//@FeignClient(name = "email-client", url = "https://api.brevo.com")
//public interface EmailClient {
//    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
//    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest body);
//}
