package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.WatchList;
import com.luv2code.CryptoTrading.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements WatchListService {

    @Autowired
    public WatchListRepository watchListRepository;

    @Override
    public WatchList findUseWatchList(Long userId) throws Exception {
        WatchList watchList = watchListRepository.findByUserId(userId);
        if (watchList == null) {
            throw new Exception("WatchList not found!");
        }
        return watchList;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList();
        watchList.setUser(user);
        return watchListRepository.save(watchList);
    }

    @Override
    public WatchList findById(Long id) throws Exception {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(id);
        if (optionalWatchList.isEmpty()) {
            throw new Exception("Watchlist not found");
        }

        return optionalWatchList.get();
    }

    @Override
    public Coin addCoinToWatchList(Coin coin, User user) throws Exception {

        WatchList watchList = findUseWatchList(user.getId());
        if (watchList.getCoinList().contains(coin)) {
            watchList.getCoinList().remove(coin);
        } else {
            watchList.getCoinList().add(coin);
        }
        watchListRepository.save(watchList);
        return coin;
    }
}
