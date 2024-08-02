package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailRepository extends JpaRepository<PaymentDetails, Long> {
    List<PaymentDetails> findByUserId(Long userId);
}
