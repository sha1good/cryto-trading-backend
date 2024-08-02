package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.Utils.OtpUtils;
import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.ForgotPasswordToken;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.VerificationCode;
import com.luv2code.CryptoTrading.request.ForgotPasswordRequest;
import com.luv2code.CryptoTrading.request.ResetPasswordRequest;
import com.luv2code.CryptoTrading.response.ApiResponse;
import com.luv2code.CryptoTrading.response.AuthResponse;
import com.luv2code.CryptoTrading.service.EmailService;
import com.luv2code.CryptoTrading.service.ForgotPasswordService;
import com.luv2code.CryptoTrading.service.UserService;
import com.luv2code.CryptoTrading.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/profiles")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByToken(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String token,
                                                              @PathVariable String otp) throws Exception {
        User user = userService.findUserByToken(token);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType(),
                    sendTo, user);
            verificationCodeService.deleteVerificationCode(verificationCode);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        throw new Exception("Wrong Otp Provided!");
    }


    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String token,
                                                      @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findUserByToken(token);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUserId(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendEmaiVerificationWithOtp(user.getEmail(), verificationCode.getOtp());

        }
        return new ResponseEntity<>("Verification Code Successfully sent!", HttpStatus.OK);
    }


    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
            @RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        User user = userService.findUserByEmail(forgotPasswordRequest.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken forgottoken = forgotPasswordService.findByUserId(user.getId());
        if (forgottoken == null) {
            forgottoken = forgotPasswordService.createToken(user, id, otp,
                    forgotPasswordRequest.getVerificationType(), forgotPasswordRequest.getSendTo());
        }

        if (forgotPasswordRequest.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendEmaiVerificationWithOtp(user.getEmail(), forgottoken.getOtp());
        }

        AuthResponse res = new AuthResponse();
        res.setSession(forgottoken.getId());
        res.setMessage("Password Reset Code Successfully sent!");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestBody ResetPasswordRequest req,

                                                     @RequestParam String id) throws Exception {

        ForgotPasswordToken reqToken = forgotPasswordService.findById(id);
        boolean isVerified = reqToken.getOtp().equals(req.getOtp());
        if (isVerified) {
            userService.updatePassword(reqToken.getUser(), req.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMessage("Password updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }
        throw new Exception("Wrong Otp");
    }
}

