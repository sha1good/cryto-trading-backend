package com.luv2code.CryptoTrading.model;


import com.luv2code.CryptoTrading.domain.PaymentMethod;
import com.luv2code.CryptoTrading.domain.PaymentOrderStatus;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PaymentOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus paymentOrderStatus;

    private PaymentMethod paymentMethod;

    @ManyToOne
    private User user;
}
