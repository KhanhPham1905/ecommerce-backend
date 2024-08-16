//package com.ghtk.ecommercewebsite.services.stripe;
//
//
//import com.stripe.Stripe;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StripeService {
//
//    @Value("$api.stripe.key")
//    private String stripeApiKey;
//
//    @PostConstruct
//    public void init() {
//        Stripe.apiKey = stripeApiKey;
//    }
//
//
//}
