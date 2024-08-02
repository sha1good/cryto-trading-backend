package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.WalletTransationType;
import com.luv2code.CryptoTrading.model.Wallet;
import com.luv2code.CryptoTrading.model.WalletTransation;
import com.luv2code.CryptoTrading.repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService{

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;


    @Override
    public WalletTransation createTransaction(Wallet wallet,
                                              WalletTransationType type,
                                              String transferId,
                                              String purpose,
                                              Long amount
    ) {
        WalletTransation transaction = new WalletTransation();
        transaction.setWallet(wallet);
        transaction.setDate(LocalDate.now());
        transaction.setWalletTransacitonType(type);
        transaction.setTransferId(transferId);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);

        return walletTransactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransation> getTransactions(Wallet wallet, WalletTransationType type) {
        return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
    }
}
