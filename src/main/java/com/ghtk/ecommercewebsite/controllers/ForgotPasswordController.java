package com.ghtk.ecommercewebsite.controllers;

import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.services.OtpService;
import com.ghtk.ecommercewebsite.utils.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forgotPassword")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final OtpService otpService;

//    @PostMapping("/verifyEmail/{email}")
//    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
//        return otpService.verifyEmailAndSendOtp(email);
//    }

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