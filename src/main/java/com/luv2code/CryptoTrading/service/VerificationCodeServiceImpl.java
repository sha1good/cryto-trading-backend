package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.Utils.OtpUtils;
import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.VerificationCode;
import com.luv2code.CryptoTrading.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService{


    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    @Override
    public VerificationCode sendVerificationCode(User user, VerificationType verificationType) {
        VerificationCode newVerificationCode = new VerificationCode();
        newVerificationCode.setOtp(OtpUtils.generateOtp());
        newVerificationCode.setVerificationType(verificationType);
        newVerificationCode.setUser(user);
        return verificationCodeRepository.save(newVerificationCode);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode> verificationCode = verificationCodeRepository.findById(id);
        if(verificationCode.isPresent()){
              return verificationCode.get();
        }
        throw new Exception("verificationCode not  found!");
    }

    @Override
    public VerificationCode getVerificationCodeByUserId(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCode(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
