package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.WatchList;

public interface  WatchListService {
    WatchList findUseWatchList(Long userId) throws Exception;
    WatchList createWatchList(User user);
    WatchList findById(Long id) throws Exception;

    Coin addCoinToWatchList(Coin coin, User user) throws Exception;


}
