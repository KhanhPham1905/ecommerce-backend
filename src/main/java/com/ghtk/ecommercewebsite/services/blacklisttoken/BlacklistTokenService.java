package com.ghtk.ecommercewebsite.services.blacklisttoken;


import com.ghtk.ecommercewebsite.models.entities.Users;
import org.springframework.stereotype.Service;

@Service
public interface BlacklistTokenService {
    void addToBlackList(String token, Users user);

    boolean isBlacklisted(String token);
}
