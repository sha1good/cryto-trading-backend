package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String>{
}
