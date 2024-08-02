package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
     public User findByEmail(String email);
}
