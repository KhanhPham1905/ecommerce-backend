package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.dtos.request.ChangePasswordDTO;
import com.ghtk.ecommercewebsite.models.dtos.request.EmailDTO;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/changePassword")
@RequiredArgsConstructor
public class ChangePasswordController {

    private final OtpService otpService;

    @PostMapping("/")
    public CommonResult<String> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        return otpService.changePasswordNewVersion(changePasswordDTO);
    }

}
