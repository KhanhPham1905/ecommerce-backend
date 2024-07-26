package com.ghtk.ecommercewebsite.repositories;

import com.ghtk.ecommercewebsite.models.entities.Token;
import com.ghtk.ecommercewebsite.models.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByUser(Users user);
    Token findByToken(String token);
    Token findByRefreshToken(String token);
}
