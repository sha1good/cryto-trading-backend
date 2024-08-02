package com.luv2code.CryptoTrading.model;


import com.luv2code.CryptoTrading.domain.VerificationType;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    @OneToOne
    private User user;

    private String email;
    private String mobile;
    private VerificationType verificationType;
}
