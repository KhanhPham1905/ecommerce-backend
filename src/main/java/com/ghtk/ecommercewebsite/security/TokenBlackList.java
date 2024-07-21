package com.ghtk.ecommercewebsite.security;

public interface TokenBlackList {
    void addToBlacklist(String token);
    boolean isBlacklisted(String token);
}
