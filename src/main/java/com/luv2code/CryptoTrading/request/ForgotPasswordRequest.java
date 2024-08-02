package com.luv2code.CryptoTrading.request;


import com.luv2code.CryptoTrading.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordRequest {

    private String sendTo; // to either user Email or Mobile
    private VerificationType verificationType;
}
