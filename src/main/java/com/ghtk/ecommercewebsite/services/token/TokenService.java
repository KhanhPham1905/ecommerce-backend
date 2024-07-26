package com.ghtk.ecommercewebsite.services.token;

import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.models.entities.Users;
import org.springframework.stereotype.Service;

@Service
public interface TokenService {
    Token addToken(Users user, String token);
    Token refreshToken(String refreshToken, Users user) throws Exception;
}
