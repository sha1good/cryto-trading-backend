package com.luv2code.CryptoTrading.model;

import com.luv2code.CryptoTrading.domain.VerificationType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ForgotPasswordToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @OneToOne
    private User user;

    private String otp;

    private VerificationType verificationType;

    private String sendTo;
}
