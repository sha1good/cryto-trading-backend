package com.luv2code.CryptoTrading.service;

public interface EmailService {
     void sendEmaiVerificationWithOtp(String userEmail, String otp) throws Exception;
}
