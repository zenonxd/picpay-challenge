package com.moreira.picpaychallenge.application.services;

import com.moreira.picpaychallenge.application.domain.entities.Transfer;
import com.moreira.picpaychallenge.application.domain.entities.User;
import com.moreira.picpaychallenge.application.domain.enums.UserType;
import com.moreira.picpaychallenge.application.domain.repositories.TransferRepository;
import com.moreira.picpaychallenge.application.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    //transacional para caso exista algum erro, o saldo volte para ambas as contas
    @Transactional
    public void checkTransfer(Long senderId, Long receiverId, BigDecimal amount) {
        //getting sender info
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (sender.getUserType() == UserType.MERCHANT) {
            throw new IllegalArgumentException("Merchants can't make any transfers.");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }

        //getting receiver info
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //creating the transfer and setting data
        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setAmount(amount);

        //setting user's new balance
        receiver.setBalance(receiver.getBalance().add(amount));
        sender.setBalance(sender.getBalance().subtract(amount));

        //saving the new balance on the database
        transferRepository.save(transfer);
        userRepository.save(receiver);
        userRepository.save(sender);
    }


}
