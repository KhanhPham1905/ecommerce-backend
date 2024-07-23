package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.common.api.CommonResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.ghtk.ecommercewebsite.services.InMemoryTokenBlacklist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LogoutController {

    public final InMemoryTokenBlacklist tokenBlacklist;

    @GetMapping("/loginAgain")
    public CommonResult<Map<String, Object>> index(HttpServletRequest request) {
        String token = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }
        tokenBlacklist.addToBlacklist(token);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Good");
        responseBody.put("status", HttpStatus.OK.value());
        return CommonResult.success(responseBody);
    }

}
