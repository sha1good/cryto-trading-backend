package com.luv2code.CryptoTrading.model;


import com.luv2code.CryptoTrading.domain.WithdrawalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private WithdrawalStatus status;

    @ManyToOne
    private User user;

    private LocalDateTime date = LocalDateTime.now();

    private Long amount;
}
