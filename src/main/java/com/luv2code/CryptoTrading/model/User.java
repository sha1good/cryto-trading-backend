package com.luv2code.CryptoTrading.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luv2code.CryptoTrading.role.USER_ROLE;
import lombok.Data;

import javax.persistence.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String Username;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private int projectSize;

     private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

     @Embedded
     private TwoFactorAuth  twofactorAuth = new TwoFactorAuth();
}
