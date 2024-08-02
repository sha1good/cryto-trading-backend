package com.luv2code.CryptoTrading.controller;

import com.luv2code.CryptoTrading.domain.WalletTransationType;
import com.luv2code.CryptoTrading.model.Order;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.Wallet;
import com.luv2code.CryptoTrading.model.Withdrawal;
import com.luv2code.CryptoTrading.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private WalletTransactionService walletTransactionService;


    //     private WithdrawalTransactionService  withdrawalTransactionService;
    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(@RequestHeader("Authorization") String token,
                                               @PathVariable Long amount) throws Exception {
        User user = userService.findUserByToken(token);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
        walletService.addBalanceToWallet(userWallet, -withdrawal.getAmount());
        walletTransactionService.createTransaction(
                userWallet, WalletTransationType.WITHDRAWAL, null, "Bank Account Withdrawal",
                withdrawal.getAmount()
        );

//        WalletTransaction walletTransaction = walletTransacitonService.createTransaction(
//                userWallet, WalletTransactionType.WITHDRAWAL, null, "Bank Account Withdrawal",
//                withdrawal.geAmount()
//        );

        return ResponseEntity.ok().body(withdrawal);

    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> processWithdrawal(@RequestHeader("Authorization") String token,
                                               @PathVariable Long id,
                                               @PathVariable boolean accept) throws Exception {
        User user = userService.findUserByToken(token);
        Withdrawal withdrawal = withdrawalService.processWithdrawal(id, accept);
        Wallet userWallet = walletService.getUserWallet(user);
        if (!accept) {
            walletService.addBalanceToWallet(userWallet, withdrawal.getAmount());
        }
        return ResponseEntity.ok().body(withdrawal);

    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<?> getUserWithdrawalHistory(@RequestHeader("Authorization") String token
    ) throws Exception {
        User user = userService.findUserByToken(token);
        List<Withdrawal> withdrawal = withdrawalService.getUserWithdrawalHistory(user);
        return ResponseEntity.ok().body(withdrawal);

    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<?> getAllWithdrawalHistory(@RequestHeader("Authorization") String token)
            throws Exception {
        User user = userService.findUserByToken(token);
        List<Withdrawal> withdrawal = withdrawalService.getAllWithdrawalRequest();
        return ResponseEntity.ok().body(withdrawal);

    }


}

