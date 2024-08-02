package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository  extends JpaRepository<PaymentOrder,Long> {
}
