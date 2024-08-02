package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.domain.OrderType;
import com.luv2code.CryptoTrading.model.Order;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.model.Wallet;
import com.luv2code.CryptoTrading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.NonUniqueResultException;
import java.math.BigDecimal;
import java.util.Optional;


@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

     public Wallet generateWallet(User user){
           Wallet wallet = new Wallet();
           wallet.setUser(user);
         return walletRepository.save(wallet);
     }
    @Override
    public Wallet getUserWallet(User user) {
     Wallet  wallet = walletRepository.findByUserId(user.getId());
      if(wallet != null){
            return wallet;
      }

        wallet = generateWallet(user);
        return wallet;
//        try {
//            Optional<Wallet> optionalWaellet = walletRepository.findByUserId(user.getId());
//            if (optionalWaellet.isEmpty()) {
//                Wallet wallet = new Wallet();
//                wallet.setUser(user);
//                walletRepository.save(wallet);
//                return wallet;
//            } else {
//                return optionalWaellet.get();
//            }
//
//        } catch (NonUniqueResultException e) {
//            // Handle the case where more than one Wallet exists for a user
//            throw new IllegalStateException("Multiple wallets found for user: " + user.getId(), e);
//        }
    }

    @Override
    public Wallet addBalanceToWallet(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();

        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> optionalWallet = walletRepository.findById(id);
        if (optionalWallet.isPresent()) {
            return optionalWallet.get();
        }
        throw new Exception("Wallet not Found!");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
        Wallet senderWallet = getUserWallet(sender);
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new Exception("InSufficient balance...");
        }
        BigDecimal newSenderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(newSenderBalance);
        walletRepository.save(senderWallet);

        BigDecimal newReceiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(newReceiverBalance);
        walletRepository.save(receiverWallet);
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {
        Wallet wallet = getUserWallet(user);
        if (order.getOrderType().equals(OrderType.BUY)) {
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new Exception("Insufficient  funds for this transaction!");
            }

            wallet.setBalance(newBalance);
        } else {
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletRepository.save(wallet);
        return wallet;
    }
}
