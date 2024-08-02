package com.luv2code.CryptoTrading.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private double quantity;

     private double buyPrice;

     @ManyToOne
     private Coin coin;

    @ManyToOne
    private User user;
}
