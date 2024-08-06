package com.ghtk.ecommercewebsite.services.blacklisttoken;

import com.ghtk.ecommercewebsite.models.entities.BlacklistToken;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.repositories.BlackListTokenRepository;
import com.ghtk.ecommercewebsite.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BlacklistTokenServiceImpl implements BlacklistTokenService{

    @Value("${security.jwt.expiration-time}")
    private int expiration;

//    public final UserService userService;
    private final BlackListTokenRepository blackListTokenRepository;

    @Override
    @Transactional
    public void addToBlackList(String token, User user) {
        long expirationInSeconds = expiration;
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds/1000);
        // Tạo mới một token cho người dùng
        BlacklistToken blacklistToken = BlacklistToken.builder()
                .user(user)
                .token(token)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .build();
        blackListTokenRepository.save(blacklistToken);
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blackListTokenRepository.findByToken(token) != null;
    }


    @Scheduled(initialDelay = 70000L, fixedDelay = 3600000L)
    public void cleanupTokens() {
        List<BlacklistToken> tokensToClean = blackListTokenRepository.findByExpirationDateBefore(LocalDateTime.now());
        System.out.println(LocalDateTime.now());
        tokensToClean.forEach(blacklistToken -> blackListTokenRepository.delete(blacklistToken));
    }
}
