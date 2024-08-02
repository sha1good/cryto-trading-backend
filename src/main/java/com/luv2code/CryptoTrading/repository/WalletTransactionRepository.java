package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.Wallet;
import com.luv2code.CryptoTrading.model.WalletTransation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletTransactionRepository extends JpaRepository<WalletTransation, Long> {
    List<WalletTransation> findByWalletOrderByDateDesc(Wallet wallet);
}
