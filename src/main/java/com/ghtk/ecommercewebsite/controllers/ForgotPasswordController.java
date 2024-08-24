package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.exceptions.DataNotFoundException;
import com.ghtk.ecommercewebsite.models.dtos.request.EmailDTO;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.OtpService;
import com.ghtk.ecommercewebsite.services.user.UserService;
import com.ghtk.ecommercewebsite.utils.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final OtpService otpService;
    private final UserService userService;

//    @PostMapping("/verifyEmail/{email}")
//    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
//        return otpService.verifyEmailAndSendOtp(email);
//    }

    @PostMapping("/verifyEmail")
    public CommonResult<String> verifyEmailNewVersion(@RequestBody EmailDTO emailDTO) throws DataNotFoundException {
        return CommonResult.success(userService.sendOtpForForgotPasswordRequest(emailDTO.getEmail()));
    }

    @PostMapping("/verifyOtp")
    public CommonResult<String> verifyOtpNewVersion() {
        return null;
    }

    @PostMapping("/verifyEmail/{email}")
    public CommonResult<String> verifyEmail(@PathVariable String email) {
       return otpService.verifyEmailAndSendOtpNewVersion(email);
    }

    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOtp(@PathVariable Integer otp, @PathVariable String email) {
        return otpService.verifyOtp(otp, email);
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<String> changePasswordHandler(@RequestBody ChangePassword changePassword,
                                                        @PathVariable String email) {
        return otpService.changePassword(changePassword, email);
    }

    @PostMapping("/resendOtp/{email}")
    public ResponseEntity<String> resendOtp(@PathVariable String email) {
        return otpService.resendOtp(email);
    }
}