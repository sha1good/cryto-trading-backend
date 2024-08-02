package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.domain.OrderStatus;
import com.luv2code.CryptoTrading.domain.OrderType;
import com.luv2code.CryptoTrading.model.*;
import com.luv2code.CryptoTrading.repository.OrderItemRepository;
import com.luv2code.CryptoTrading.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AssetService assetService;

    @Override
    public Order getOrderId(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found!"));
    }

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderType(orderType);
        order.setOrderItem(orderItem);
        order.setTimestamp(LocalDateTime.now());
        order.setPrice(BigDecimal.valueOf(price));
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrderOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception {
        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin, quantity, user);
        }

        throw new Exception("InValid Order Type");
    }

    private OrderItem createOrderItem(Coin coin, double quantity, double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();

        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem);
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity < 0) {
            throw new Exception("Quantity should be > 0");
        }
        double buyPrice = coin.getCurrentPrice();
        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);
        walletService.payOrderPayment(order, user);
        order.setStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        // Create an asset
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),
                order.getOrderItem().getCoin().getId());

        if (oldAsset == null) {
            assetService.createAsset(user, orderItem.getCoin(), orderItem.getQuantity());
        } else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;
    }


    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) throws Exception {
        if (quantity < 0) {
            throw new Exception("Quantity should be > 0");
        }
        double sellPrice = coin.getCurrentPrice();

        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());

        double buyPrice = assetToSell.getBuyPrice();
        if (assetToSell != null) {
            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);

            Order order = createOrder(user, orderItem, OrderType.SELL);
            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setStatus(OrderStatus.SUCCESS);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);
                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
// Create an asset
                return savedOrder;
            }
            throw new Exception("InSufficient Quantity to sell");
        }
        throw new Exception("Asset not  found!");
    }
}