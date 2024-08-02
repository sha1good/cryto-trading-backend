package com.luv2code.CryptoTrading.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coin {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image")
    private String image;
    @JsonProperty("current_Price")
    private double currentPrice;
    @JsonProperty("market_Cap")
    private long marketCap;
    @JsonProperty("market_Cap_Rank")
    private int marketCapRank;
    @JsonProperty("fully_Diluted_Valuation")
    private long fullyDilutedValuation;
    @JsonProperty("total_volume")
    private long totalVolume;
    @JsonProperty("high_24h")
    private double high24h;
    @JsonProperty("low_24h")
    private double low24h;
    @JsonProperty("price_Change_24h")
    private double priceChange24h;
    @JsonProperty("price_Change_Percentage_24h")
    private double priceChangePercentage24h;
    @JsonProperty("market_Cap_Change_24h")
    private long marketCapChange24h;
    @JsonProperty("market_Cap_Change_Percentage_24h")
    private double marketCapChangePercentage24h;
    @JsonProperty("circulating_Supply")
    private long circulatingSupply;
    @JsonProperty("total_supply")
    private long totalSupply;
    @JsonProperty("max_Supply")
    private long maxSupply;
    @JsonProperty("ath")
    private double ath;
    @JsonProperty("ath_Change_Percentage")
    private double athChangePercentage;
    @JsonProperty("ath_date")
    private Date athDate;
    @JsonProperty("atl")
    private double atl;
    @JsonProperty("atl_Change_Percentage")
    private double atlChangePercentage;
    @JsonProperty("atl_date")
    private Date atlDate;
    @JsonProperty("roi")
    @JsonIgnore
    private String roi;
    @JsonProperty("last_updated")
    private Date lastUpdated;
}
