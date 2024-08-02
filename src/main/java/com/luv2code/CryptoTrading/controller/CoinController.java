package com.luv2code.CryptoTrading.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {
    @Autowired
    private CoinService coinService;

    @Autowired
    private ObjectMapper objMapper;

    @GetMapping
    public ResponseEntity<List<Coin>> getCoinList(@RequestParam(required = false, name = "page") int page) throws Exception {
        List<Coin> coinList = coinService.getCoinList(page);
        return new ResponseEntity<>(coinList, HttpStatus.CREATED);
    }


    @GetMapping("/{coinId}/chart")
    public ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinId, @RequestParam("days") int days)
            throws Exception {
        String res = coinService.getMarketChart(coinId, days);
        JsonNode jsonNode = objMapper.readTree(res);
        return new ResponseEntity<>(jsonNode, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword)
            throws Exception {
        String res = coinService.searchCoin(keyword);
        JsonNode jsonNode = objMapper.readTree(res);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/top50")
    public ResponseEntity<JsonNode> getTop50ByMarketCapRank()
            throws Exception {
        String res = coinService.getTo50CoinsByMarketCapRank();
        JsonNode jsonNode = objMapper.readTree(res);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/treading")
    public ResponseEntity<JsonNode> getTreadingCoin()
            throws Exception {
        String res = coinService.getTrendingCoins();
        JsonNode jsonNode = objMapper.readTree(res);
        return ResponseEntity.ok(jsonNode);
    }

    @GetMapping("/details/{coinId}")
    public ResponseEntity<JsonNode> getCoinDetails(@PathVariable String coinId)
            throws Exception {
        String coin = coinService.coinDetails(coinId);
        JsonNode jsonNode = objMapper.readTree(coin);
        return ResponseEntity.ok(jsonNode);
    }


}
