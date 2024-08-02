package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.VerificationCode;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id) throws Exception;
    VerificationCode getVerificationCodeByUserId(Long userId);
    void deleteVerificationCode(VerificationCode verificationCode);
}
