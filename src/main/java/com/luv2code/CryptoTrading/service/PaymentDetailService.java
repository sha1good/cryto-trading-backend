package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.PaymentDetails;
import com.luv2code.CryptoTrading.model.User;

import java.util.List;

public interface PaymentDetailService {

    PaymentDetails addPaymentDetails(String accountNumber,
                                     String accountHolderName,
                                     String ifsc,
                                     String bankName,
                                     User user);

    List<PaymentDetails> getUserPaymentDetails(User user);
}
