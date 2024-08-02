package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    WatchList findByUserId(Long userId);
}
