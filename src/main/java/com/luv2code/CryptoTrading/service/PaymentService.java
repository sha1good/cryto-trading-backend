package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.PaymentMethod;
import com.luv2code.CryptoTrading.model.PaymentOrder;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.response.PaymentResponse;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod);
    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException;

    PaymentResponse  createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException;
    PaymentResponse  createStripePayPaymentLink(User user, Long amount, Long orderId) throws StripeException;
}
