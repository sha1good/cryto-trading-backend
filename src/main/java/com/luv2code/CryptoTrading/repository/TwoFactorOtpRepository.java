package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp, String> {
    TwoFactorOtp findByUserId(Long userId);
}
