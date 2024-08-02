package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.OrderType;
import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.Order;
import com.luv2code.CryptoTrading.model.OrderItem;
import com.luv2code.CryptoTrading.model.User;

import java.util.List;

public interface OrderService {
    Order getOrderId(Long orderId) throws Exception;
    Order createOrder(User user, OrderItem orderItem, OrderType orderType);
    List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol);
    Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;
}
