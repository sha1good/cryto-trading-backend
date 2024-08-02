package com.luv2code.CryptoTrading.service;


import com.luv2code.CryptoTrading.domain.VerificationType;
import com.luv2code.CryptoTrading.model.User;
import org.springframework.stereotype.Service;

public interface UserService {

    User findUserByToken(String token) throws Exception;

    User findUserById(Long userId) throws Exception;

    User findUserByEmail(String email) throws Exception;

    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);

    User updatePassword(User user, String newPassword);


}
