package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.Coin;

import java.util.List;

public interface CoinService {

    List<Coin> getCoinList(int page) throws Exception;

    String getMarketChart( String coinId, int days) throws Exception;

    String coinDetails(String coinId) throws Exception;

    Coin findById(String coinId) throws Exception;

    String searchCoin(String  keyword) throws Exception;

    String getTo50CoinsByMarketCapRank() throws Exception;
    String getTrendingCoins() throws Exception;
}
