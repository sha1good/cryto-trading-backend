package com.luv2code.CryptoTrading.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String payment_link_url;
    private String payment_intent;
}
