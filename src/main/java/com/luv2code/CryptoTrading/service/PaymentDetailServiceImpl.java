package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.PaymentDetails;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.PaymentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber,
                                            String accountHolderName, String ifsc, String bankName, User user) {
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHolder(accountHolderName);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);
        paymentDetails.setIfsc(ifsc);
        return paymentDetailRepository.save(paymentDetails);
    }

    @Override
    public List<PaymentDetails> getUserPaymentDetails(User user) {
        return paymentDetailRepository.findByUserId(user.getId());
    }
}
