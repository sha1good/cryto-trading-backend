package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.domain.PaymentMethod;
import com.luv2code.CryptoTrading.model.PaymentOrder;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.response.PaymentResponse;
import com.luv2code.CryptoTrading.service.PaymentService;
import com.luv2code.CryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @RequestHeader("Authorization") String token, @PathVariable PaymentMethod paymentMethod,
            @PathVariable Long amount
    ) throws Exception {
       User user = userService.findUserByToken(token);

       PaymentResponse paymentResponse;

       PaymentOrder paymentOrder = paymentService.createPaymentOrder(user, amount, paymentMethod);
       if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
           paymentResponse = paymentService.createRazorPayPaymentLink(user, amount,paymentOrder.getId());
       }else {
             paymentResponse = paymentService.createStripePayPaymentLink(user, amount,paymentOrder.getId());
       }

       return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
}
