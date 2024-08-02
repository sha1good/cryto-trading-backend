package com.luv2code.CryptoTrading.service;

import com.luv2code.CryptoTrading.config.JwtProvider;
import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.TwoFactorAuth;
import com.luv2code.CryptoTrading.model.User;
import com.luv2code.CryptoTrading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceimpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByToken(String token) throws Exception {
        String email = JwtProvider.getEmailFromToken(token);
        return findUserByEmail(email);
    }

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new Exception("User not found!");
        }
        return optionalUser.get();
    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("User not found!");
        }
        return user;
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setVerificationType(verificationType);
        user.setTwofactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }


}
