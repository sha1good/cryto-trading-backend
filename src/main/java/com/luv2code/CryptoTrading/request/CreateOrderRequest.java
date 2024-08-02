package com.luv2code.CryptoTrading.request;

import com.luv2code.CryptoTrading.domain.OrderType;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private String coinId;
    private double quantity;
    private OrderType orderType;
}
