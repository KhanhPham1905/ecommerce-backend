package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.user.UserService;
import com.ghtk.ecommercewebsite.services.blacklisttoken.BlacklistTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LogoutController {
    public final UserService userService;
    public final BlacklistTokenService blacklistTokenService;

//    @GetMapping("/loginAgain")
//    public CommonResult<Map<String, Object>> index(HttpServletRequest request) throws Exception{
//        System.out.println("qua lowp login again");
//        String token = null;
//        String authorizationHeader = request.getHeader("Authorization");
//        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
//            token = authorizationHeader.substring(7);
//            User userDetail = userService.getUserDetailsFromToken(token);
//            blacklistTokenService.addToBlackList(token, userDetail);
//        }
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("message", "Good");
//        responseBody.put("status", HttpStatus.OK.value());
//        return CommonResult.success(responseBody);
//    }

    @GetMapping("/logout-admin")
    public CommonResult<Map<String, Object>> logoutAdmin(HttpServletRequest request) throws Exception{
        String token = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            User userDetail = userService.getUserDetailsFromToken(token);
            blacklistTokenService.addToBlackList(token, userDetail);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "logout admin");
        responseBody.put("status", HttpStatus.OK.value());
        return CommonResult.success(responseBody);
    }

    @GetMapping("/logout-seller")
    public CommonResult<Map<String, Object>> logoutSeller(HttpServletRequest request) throws Exception{
        String token = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            User userDetail = userService.getUserDetailsFromToken(token);
            blacklistTokenService.addToBlackList(token, userDetail);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "logout seller");
        responseBody.put("status", HttpStatus.OK.value());
        return CommonResult.success(responseBody);
    }

    @GetMapping("/logout-user")
    public CommonResult<Map<String, Object>> logoutUser(HttpServletRequest request) throws Exception{
        String token = null;
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            User userDetail = userService.getUserDetailsFromToken(token);
            blacklistTokenService.addToBlackList(token, userDetail);
        }
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "logout user");
        responseBody.put("status", HttpStatus.OK.value());
        return CommonResult.success(responseBody);
    }
}
