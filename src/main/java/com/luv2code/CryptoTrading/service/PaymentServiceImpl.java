package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.PaymentMethod;
import com.luv2code.CryptoTrading.domain.PaymentOrderStatus;
import com.luv2code.CryptoTrading.model.PaymentOrder;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.PaymentRepository;
import com.luv2code.CryptoTrading.response.PaymentResponse;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentOrder createPaymentOrder(User user, Long amount, PaymentMethod paymentMethod) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setUser(user);
        paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
        return paymentRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        return paymentRepository.findById(id).orElseThrow(() -> new Exception("Payment Order not found!"));
    }

    @Override
    public Boolean processPaymentOrder(PaymentOrder paymentOrder, String paymentId)
            throws RazorpayException {

        if (paymentOrder.getPaymentOrderStatus() == null) {
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
        }
        if (paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");
                if (status.equals("captured")) {
                    paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }

                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
                paymentRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
            paymentRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorPayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        Long Amount = amount * 100;
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject paymentLinkReq = new JSONObject();
            paymentLinkReq.put("amount", amount);
            paymentLinkReq.put("currency", "cad");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFullName());
            customer.put("email", user.getEmail());
            paymentLinkReq.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkReq.put("notify", notify);
            paymentLinkReq.put("reminder_enable", true);

            paymentLinkReq.put("callback_url", "http://localhost:5173/wallet?order_id=" + orderId);
            paymentLinkReq.put("callback_method", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkReq);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse response = new PaymentResponse();
            response.setPayment_link_url(paymentLinkUrl);
            return response;
        } catch (Exception e) {
            System.out.println("Error creating payment Link : " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }

    }

    public PaymentIntent createPaymentIntent(Long amount) throws StripeException {
        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setCurrency("cad")
                .setAmount(amount * 100) // Amount in cents
                .build();

        return PaymentIntent.create(createParams);
    }

    @Override
    public PaymentResponse createStripePayPaymentLink(User user, Long amount, Long orderId) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        // Create PaymentIntent
        PaymentIntent paymentIntent = createPaymentIntent(amount);

        // Create Checkout Session
        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId + "&payment_intent=" + paymentIntent.getId())
                .setCancelUrl("http://localhost:5173/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("cad")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Top up wallet")
                                        .build())
                                .build())
                        .build())
                .build();

        Session session = Session.create(params);
        System.out.println("Session ============== " + session);

        // Create Payment Response
        PaymentResponse response = new PaymentResponse();
        response.setPayment_link_url(session.getUrl());
        return response;
    }

}

//    public PaymentIntent createPaymentIntent(Long amount) throws StripeException {
//        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
//                .setCurrency("cad")
//                .setAmount(amount * 100) // Amount in cents
//                .build();
//
//        return PaymentIntent.create(createParams);
//    }
//
//    @Override
//    public PaymentResponse createStripePayPaymentLink(User user, Long amount, Long orderId) throws StripeException {
//        Stripe.apiKey = stripeSecretKey;
//        // Create PaymentIntent
//        PaymentIntent paymentIntent = createPaymentIntent(amount);
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId + "&payment_intent=" + paymentIntent.getId())
//                .setCancelUrl("http://localhost:5173/payment/cancel")
//                .addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L).
//                        setPriceData(SessionCreateParams.
//                                LineItem.PriceData.builder().setCurrency("cad").
//                                setUnitAmount(amount * 100)
//                                .setProductData(SessionCreateParams.LineItem.
//                                        PriceData.ProductData.builder().
//                                        setName("Top up wallet").build()).build()).build())
//                .setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder().setId(paymentIntent.getId())
//                .build();
//        Session session = Session.create(params);
//        System.out.println("Session ============== " + session);
//        PaymentResponse response = new PaymentResponse();
//        response.setPayment_link_url(session.getUrl());
//        return response;
//    }




