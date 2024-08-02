package com.luv2code.CryptoTrading.repository;

import com.luv2code.CryptoTrading.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository  extends JpaRepository<OrderItem, Long> {
}
