package com.luv2code.CryptoTrading.controller;

import com.luv2code.CryptoTrading.model.PaymentDetails;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.service.PaymentDetailService;
import com.luv2code.CryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailService paymentDetailService;

    @PostMapping("/payment-details")
    public ResponseEntity<?> addPaymentDetails(@RequestHeader("Authorization") String token,
                                               @RequestBody PaymentDetails paymentDetails) throws Exception {
        User user = userService.findUserByToken(token);
        PaymentDetails newpaymentDetails = paymentDetailService.addPaymentDetails(
                paymentDetails.getAccountNumber(), paymentDetails.getAccountHolder(), paymentDetails.getIfsc(),
                paymentDetails.getBankName(), user
        );
        return new ResponseEntity<>(newpaymentDetails, HttpStatus.CREATED);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<?>  getPaymentDetails(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByToken(token);
        List<PaymentDetails> newpaymentDetails = paymentDetailService.getUserPaymentDetails(user);
        return new ResponseEntity<>(newpaymentDetails, HttpStatus.OK);
    }
}
