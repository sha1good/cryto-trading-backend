package com.luv2code.CryptoTrading.controller;


import com.luv2code.CryptoTrading.model.Asset;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.service.AssetService;
import com.luv2code.CryptoTrading.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @GetMapping("/{assetId}")
    public ResponseEntity<Asset> getAssetById(@RequestHeader("Authorization") String token,
                                              @PathVariable Long assetId) throws Exception {
        Asset asset = assetService.getAssetById(assetId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping("/coin/{coinId}/user")
    public ResponseEntity<Asset> getAssetByUserIdAndCoinId(@RequestHeader("Authorization") String token,
                                              @PathVariable String coinId) throws Exception {
        User user = userService.findUserByToken(token);
        Asset asset = assetService.findAssetByUserIdAndCoinId(user.getId(), coinId);
        return ResponseEntity.ok().body(asset);
    }

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssetForUser(@RequestHeader("Authorization") String token,
                                                           @PathVariable Long coinId) throws Exception {
        User user = userService.findUserByToken(token);
        List<Asset> assets = assetService.getUsersAssets(user.getId());
        return ResponseEntity.ok().body(assets);
    }


}
