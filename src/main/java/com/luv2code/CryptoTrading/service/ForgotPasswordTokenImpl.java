package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.ForgotPasswordToken;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.ForgotPasswordTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordTokenImpl implements ForgotPasswordService {

    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;


    @Override
    public ForgotPasswordToken createToken(User user, String id, String otp,
                                           VerificationType verificationType, String sendTo) {

        ForgotPasswordToken token = new ForgotPasswordToken();

        token.setId(id);
        token.setOtp(otp);
        token.setSendTo(sendTo);
        token.setVerificationType(verificationType);
        token.setUser(user);
        return forgotPasswordTokenRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordTokenRepository.findById(id);
        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUserId(Long userId) {

        return forgotPasswordTokenRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken fForgotPasswordToken) {
        forgotPasswordTokenRepository.delete(fForgotPasswordToken);
    }
}
