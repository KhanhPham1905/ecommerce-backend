package com.ghtk.ecommercewebsite.services.token;

import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.models.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    Token addToken(User user, String token);
    Token refreshToken(String refreshToken, User user) throws Exception;
}
