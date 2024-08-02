package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.domain.OrderType;
import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.Order;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.request.CreateOrderRequest;
import com.luv2code.CryptoTrading.service.CoinService;
import com.luv2code.CryptoTrading.service.OrderService;
import com.luv2code.CryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

//    @Autowired
//    private WalletServiceTransactionService walletServiceTransactionService;

    @Autowired
    private CoinService coinService;


//    public OrderController(OrderService orderService, UserService userService) {
//        this.orderService = orderService;
//        this.userService = userService;
//    }

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String token,
                                                 @RequestBody
                                                         CreateOrderRequest req) throws Exception {
        User user = userService.findUserByToken(token);
        Coin coin = coinService.findById(req.getCoinId());
        Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String token,
                                              @PathVariable Long orderId) throws Exception {
        User user = userService.findUserByToken(token);
        Order order = orderService.getOrderId(orderId);
        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        } else {
            //return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            throw new Exception("You do not have access!");
        }

    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrderForUser(@RequestHeader("Authorization") String token,
                                                          @RequestParam(required = false) OrderType order_type,
                                                          @RequestParam(required = false) String asset_symbol) throws Exception {
        Long userId = userService.findUserByToken(token).getId();
        List<Order> userOrders = orderService.getAllOrderOfUser(userId, order_type, asset_symbol);
        return ResponseEntity.ok(userOrders);
    }

}
