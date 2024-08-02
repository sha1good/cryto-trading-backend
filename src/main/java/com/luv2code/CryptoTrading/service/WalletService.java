package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.Order;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.Wallet;

import java.util.List;

public interface WalletService {

    Wallet getUserWallet(User user);
    Wallet addBalanceToWallet(Wallet wallet, Long money);
    Wallet findWalletById(Long id) throws Exception;
    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;
    Wallet payOrderPayment(Order order, User user) throws Exception;
}
