package com.luv2code.CryptoTrading.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
public class TwoFactorOtp {
    @Id
    private String id;

    private String otp;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne
    private User user;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String jwt;
}
