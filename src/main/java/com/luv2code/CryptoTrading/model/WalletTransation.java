package com.luv2code.CryptoTrading.model;

import com.luv2code.CryptoTrading.domain.WalletTransationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Wallet wallet;

    private WalletTransationType walletTransacitonType;

    private LocalDate date;

    private String transferId;

    private String purpose;

    private Long amount;
}
