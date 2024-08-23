package com.ghtk.ecommercewebsite.services;

import com.ghtk.ecommercewebsite.models.dtos.MailBody;
import com.ghtk.ecommercewebsite.models.entities.ForgotPassword;
import com.ghtk.ecommercewebsite.models.entities.User;
import com.ghtk.ecommercewebsite.models.responses.CommonResult;
import com.ghtk.ecommercewebsite.repositories.ForgotPasswordRepository;
import com.ghtk.ecommercewebsite.repositories.UserRepository;
import com.ghtk.ecommercewebsite.utils.ChangePassword;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisOtpService redisOtpService;

    // New version
    public CommonResult<String> verifyEmailAndSendOtpNewVersion(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

        int otp = redisOtpService.generateAndSaveOtp(email);
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password request")
                .build();
        emailService.sendSimpleMessage(mailBody);
        // Return the email for the verification step
        return CommonResult.success(email, "Email sent for verification!");
    }

//    public CommonResult<String> verifyOtpForForgotPasswordRequest(Integer otp, String email) {
//        boolean isValid = redisOtpService.verifyOtp(email, otp);
//        if (isValid) {
//            return ResponseEntity.ok("OTP verified!");
//        }
//        return new ResponseEntity<>("Invalid or expired OTP!", HttpStatus.BAD_REQUEST);
//    }

    public ResponseEntity<String> verifyEmailAndSendOtp(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));

//        int otp = otpGenerator();
        int otp = redisOtpService.generateAndSaveOtp(email);
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is the OTP for your Forgot Password request: " + otp)
                .subject("OTP for Forgot Password request")
                .build();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .user(user)
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification!");
//        try {
//            emailService.sendSimpleMessage(mailBody);
//            forgotPasswordRepository.save(fp);
//            return ResponseEntity.ok("Email sent for verification!");
//        } catch (Exception e) {
//            // Log the exception
//            logger.error("Error sending email: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
//        }
    }

    public ResponseEntity<String> verifyOtp(Integer otp, String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Please provide a valid email!"));
//
//        ForgotPassword fp = forgotPasswordRepository.findByOtpAndUser(otp, user)
//                .orElseThrow(() -> new RuntimeException("Invalid OTP for email " + email));
//        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
//            forgotPasswordRepository.deleteById(fp.getFpid());
//            return new ResponseEntity<>("OTP has expired!", HttpStatus.EXPECTATION_FAILED);
//        }
//
//        forgotPasswordRepository.deleteById(fp.getFpid());
//        return ResponseEntity.ok("OTP verified!");
        boolean isValid = redisOtpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified!");
        }
        return new ResponseEntity<>("Invalid or expired OTP!", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> changePassword(ChangePassword changePassword, String email) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.EXPECTATION_FAILED);
        }
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        userRepository.updatePassword(email, encodedPassword);

        return ResponseEntity.ok("Password has been changed!");
    }

//    private Integer otpGenerator() {
//        Random random = new Random();
//        return random.nextInt(100_000, 999_999);
//    }

    public ResponseEntity<String> resendOtp(String email) {
        try {
            int otp = redisOtpService.generateAndSaveOtp(email);

            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("This is the OTP for your request: " + otp)
                    .subject("OTP for Forgot Password request")
                    .build();

            emailService.sendSimpleMessage(mailBody);
            return ResponseEntity.ok("OTP has been resent!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many OTP requests. Please wait before trying again.");
        }
    }

    public CommonResult<String> resendOtpForSigningUp(String email) {
        try {
            int otp = redisOtpService.generateAndSaveOtp(email);

            MailBody mailBody = MailBody.builder()
                    .to(email)
                    .text("This is the OTP for your Forgot Password request: " + otp)
                    .subject("OTP for Forgot Password request")
                    .build();

            emailService.sendSimpleMessage(mailBody);
            return CommonResult.success("OTP has been resent!");
        } catch (RuntimeException e) {
            return CommonResult.tooManyRequests("Too many OTP requests. Please wait before trying again.");
        }
    }

}
