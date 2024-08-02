package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.TwoFactorOtp;
import com.luv2code.CryptoTrading.model.User;

public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String Id);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);
}
