package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.domain.WalletTransationType;
import com.luv2code.CryptoTrading.model.*;
import com.luv2code.CryptoTrading.response.PaymentResponse;
import com.luv2code.CryptoTrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private WalletTransactionService walletTransactionService;


    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByToken(token);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/wallet/transactions")
    public ResponseEntity<List<WalletTransation>> getWalletTransaction(
            @RequestHeader("Authorization")String token) throws Exception {
        User user=userService.findUserByToken(token);

        Wallet wallet = walletService.getUserWallet(user);

        List<WalletTransation> transactions=walletTransactionService.getTransactions(wallet,null);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String token,
                                                         @PathVariable Long walletId,
                                                         @RequestBody WalletTransation req)
            throws Exception {
        User senderUser = userService.findUserByToken(token);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser, receiverWallet, req.getAmount());
        walletTransactionService.createTransaction(
                wallet,
                WalletTransationType.WALLET_TRANSFER,receiverWallet.getId().toString(),
                req.getPurpose(),
                -req.getAmount()
        );
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long orderId)
            throws Exception {
        User user = userService.findUserByToken(token);
        Order order = orderService.getOrderId(orderId);
        Wallet wallet = walletService.payOrderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addMoneyToUserWallet(@RequestHeader("Authorization") String token,
                                                       @RequestParam("order_id") Long paymentOrderId,
                                                       @RequestParam("payment_intent") String paymentId) throws Exception {
        User user = userService.findUserByToken(token);

        Wallet userWallet = walletService.getUserWallet(user);
        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);
        Boolean status = paymentService.processPaymentOrder(paymentOrder, paymentId);

        System.out.println("Status + ============ " +  status);
        PaymentResponse res = new PaymentResponse();
        res.setPayment_link_url("deposite success");

        if (status) {
            userWallet = walletService.addBalanceToWallet(userWallet, paymentOrder.getAmount());
        }
        return new ResponseEntity<>(userWallet, HttpStatus.ACCEPTED);

    }
}
