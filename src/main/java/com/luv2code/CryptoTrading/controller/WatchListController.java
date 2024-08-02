package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.WatchList;
import com.luv2code.CryptoTrading.service.CoinService;
import com.luv2code.CryptoTrading.service.UserService;
import com.luv2code.CryptoTrading.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    @Autowired
    private WatchListService watchListService;
    @Autowired
    private UserService userService;
    @Autowired
    private CoinService coinService;


    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader("Authorization") String token) throws Exception {
        User user = userService.findUserByToken(token);
        WatchList watchList = watchListService.findUseWatchList(user.getId());
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(
            @PathVariable Long watchListId) throws Exception {
        WatchList watchList = watchListService.findById(watchListId);
        return new ResponseEntity<>(watchList, HttpStatus.OK);
    }

    @PatchMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String token,
                                                   @PathVariable String coinId) throws Exception {
        User user = userService.findUserByToken(token);
        Coin coin = coinService.findById(coinId);
        Coin addedCoin = watchListService.addCoinToWatchList(coin, user);
        return  ResponseEntity.ok(addedCoin);
    }
}
