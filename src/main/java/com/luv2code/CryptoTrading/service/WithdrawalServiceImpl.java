package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.domain.WithdrawalStatus;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.Withdrawal;
import com.luv2code.CryptoTrading.repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {
    @Autowired
    public WithdrawalRepository withdrawalRepository;

    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withDraw = new Withdrawal();
        withDraw.setAmount(amount);
        withDraw.setStatus(WithdrawalStatus.PENDING);
        withDraw.setUser(user);
        return withdrawalRepository.save(withDraw);
    }

    @Override
    public Withdrawal processWithdrawal(Long withdrawalId, boolean accept) throws Exception {
        Optional<Withdrawal> withdraw = withdrawalRepository.findById(withdrawalId);
        if (withdraw.isEmpty()) {
            throw new Exception("Withdrawal Not found!");
        }

        Withdrawal newWithdrawal = withdraw.get();
        if (accept) {
            newWithdrawal.setStatus(WithdrawalStatus.SUCCESS);
        } else {
            newWithdrawal.setStatus(WithdrawalStatus.DECLINE);
        }
        return withdrawalRepository.save(newWithdrawal);
    }

    @Override
    public List<Withdrawal> getUserWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return  withdrawalRepository.findAll();
    }
}
