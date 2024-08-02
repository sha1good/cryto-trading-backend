package com.luv2code.CryptoTrading.model;


import com.luv2code.CryptoTrading.domain.VerificationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerificationType verificationType;
}
