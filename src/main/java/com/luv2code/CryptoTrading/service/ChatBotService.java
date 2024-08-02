package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.model.CoinDTO;
import com.luv2code.CryptoTrading.response.ApiResponse;

public interface ChatBotService {
    ApiResponse getCoinDetails(String coinName);

    CoinDTO getCoinByName(String coinName);

    String simpleChat(String prompt);
}
