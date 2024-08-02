package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.model.Asset;
import com.luv2code.CryptoTrading.model.Coin;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset createAsset(User user, Coin coin, double quantity) {
        Asset  asset = new Asset();
        asset.setBuyPrice(coin.getCurrentPrice());
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setUser(user);

        return  assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {
        return assetRepository.findById(assetId).orElseThrow(() -> new Exception("Asset not found!"));
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUsersAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {
        Asset oldAsset =  getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());
        return  assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {

        return assetRepository.findByUserIdAndCoinId(userId, coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);
    }

}
