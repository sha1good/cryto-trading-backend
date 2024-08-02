package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.ForgotPasswordToken;
import com.luv2code.CryptoTrading.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String id, String otp,
                                    VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUserId(Long userId);

    void deleteToken(ForgotPasswordToken fForgotPasswordToken);
}
