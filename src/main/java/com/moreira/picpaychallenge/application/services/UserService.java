package com.moreira.picpaychallenge.application.services;

import com.moreira.picpaychallenge.domain.entities.User;
import com.moreira.picpaychallenge.domain.enums.UserType;
import com.moreira.picpaychallenge.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void validateTransaction(User sender, BigDecimal amount) {

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new IllegalArgumentException("Merchants can't make any transfers.");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
    }

    public User findUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
