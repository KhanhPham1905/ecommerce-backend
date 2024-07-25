package com.ghtk.ecommercewebsite.services.blacklisttoken;


import com.ghtk.ecommercewebsite.models.entities.User;
import org.springframework.stereotype.Service;

@Service
public interface BlacklistTokenService {
    void addToBlackList(String token, User user);

    boolean isBlacklisted(String token);


}
