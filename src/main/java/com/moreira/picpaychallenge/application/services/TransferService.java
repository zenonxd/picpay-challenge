package com.moreira.picpaychallenge.application.services;

import com.moreira.picpaychallenge.application.dto.TransactionDTO;
import com.moreira.picpaychallenge.domain.entities.Transfer;
import com.moreira.picpaychallenge.domain.entities.User;
import com.moreira.picpaychallenge.domain.repositories.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    @Autowired
    private TransferAuthorizationService transferAuthorizationService;

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private EmailNotificationService emailNotificationService;
    @Autowired
    private NotificationService notificationService;

    //transacional para caso exista algum erro, o saldo volte para ambas as contas
    @Transactional
    public void transfer(TransactionDTO transaction) {

        User sender = userService.findUserById(transaction.senderId());

        User receiver = userService.findUserById(transaction.receiverId());

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorized = transferAuthorizationService.authorizeTransfer(sender.getId(), receiver.getId(), transaction.value());
        if (!isAuthorized) {
            throw new RuntimeException("Transfer authorization failed.");
        }

        //creating the transfer and setting data
        Transfer transfer = new Transfer();
        transfer.setSender(sender);
        transfer.setReceiver(receiver);
        transfer.setAmount(transaction.value());
        transfer.setTimestamp(LocalDateTime.now());


        //setting user's new balance
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        //saving the new balance on the database
        transferRepository.save(transfer);
        userService.saveUser(receiver);
        userService.saveUser(sender);

        notificationService.sendTransferNotification(sender, receiver, transaction.value());

    }


}
