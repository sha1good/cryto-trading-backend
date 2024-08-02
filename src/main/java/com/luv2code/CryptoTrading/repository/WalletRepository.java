package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
//    @Query("SELECT w FROM wallet w where w.user.id = :userId")
//    Optional<Wallet> findByUserId(Long userId);

    Wallet findByUserId(Long userId);
}
