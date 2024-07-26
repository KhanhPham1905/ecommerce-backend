package com.ghtk.ecommercewebsite.repositories;


import com.ghtk.ecommercewebsite.models.entities.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BlackListTokenRepository extends JpaRepository<BlacklistToken, Long> {

    BlacklistToken findByToken(String token);
    List<BlacklistToken> findByExpirationDateBefore(LocalDateTime time);

}
