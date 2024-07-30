package com.ghtk.ecommercewebsite.utils;

public class WhitelistUrls {
    public static final String[] URLS = {
            "/api/v1/user/login",
            "/api/v1/user/signup",
            "/api/v1/seller/signup",
            "/api/v1/seller/login",
            "/api/v1/admin/login",
            "/forgotPassword/**",
            "/api/products/**"
    };
}
