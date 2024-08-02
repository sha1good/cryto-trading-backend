package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amkount, User user);

    Withdrawal processWithdrawal(Long withdrawalId, boolean accept) throws Exception;

    List<Withdrawal> getUserWithdrawalHistory(User user);

    List<Withdrawal> getAllWithdrawalRequest();
}
