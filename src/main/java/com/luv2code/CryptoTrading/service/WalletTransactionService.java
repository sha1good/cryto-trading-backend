package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.WalletTransationType;
import com.luv2code.CryptoTrading.model.Wallet;
import com.luv2code.CryptoTrading.model.WalletTransation;

import java.util.List;

public interface WalletTransactionService {

    WalletTransation createTransaction(Wallet wallet,
                                       WalletTransationType type,
                                       String transferId,
                                       String purpose,
                                       Long amount
    );

    List<WalletTransation> getTransactions(Wallet wallet, WalletTransationType type);
}
