package com.moreira.picpaychallenge.application.services;

import com.moreira.picpaychallenge.application.dto.UserRequestDTO;
import com.moreira.picpaychallenge.application.dto.UserResponseDTO;
import com.moreira.picpaychallenge.domain.entities.User;
import com.moreira.picpaychallenge.domain.enums.UserType;
import com.moreira.picpaychallenge.domain.exceptions.*;
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
            throw new UserTypeException("Merchants can't make any transfers.");
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Sender has insufficient funds.");
        }

        if (sender.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidTransactionAmountException("The transaction amount cannot be negative.");
        }

    }

    public void validateUserFields(UserRequestDTO userRequestDTO) {

        if (userRequestDTO.firstName().isBlank()) {
            throw new InvalidNameException("First name cannot be empty.");
        }

        if (userRequestDTO.firstName().length() < 2) {
            throw new InvalidNameException("First name must be at least 2 characters.");
        }

        if (userRequestDTO.lastName().isBlank()) {
            throw new ParameterNotFilledException("Last name is mandatory.");
        }

        if (userRequestDTO.email().isBlank()) {
            throw new ParameterNotFilledException("Email is mandatory.");
        }

        if (userRepository.existsByEmail(userRequestDTO.email())) {
            throw new EmailNotValidException("Email is already in use.");
        }

        if (userRepository.existsByDocument(userRequestDTO.document())) {
            throw new DocumentAlreadyUsedException("Document is already in use.");
        }

        if (userRequestDTO.document().isBlank()) {
            throw new ParameterNotFilledException("Document is mandatory.");
        }

        if (userRequestDTO.password().isBlank()) {
            throw new ParameterNotFilledException("Password is mandatory.");
        }

        if (userRequestDTO.password().length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters.");
        }
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        validateUserFields(userRequestDTO);


        User user = new User();
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setDocument(userRequestDTO.document());
        user.setEmail(userRequestDTO.email());
        user.setPassword(userRequestDTO.password());
        user.setUserType(userRequestDTO.userType());
        user.setBalance(userRequestDTO.balance());


        userRepository.save(user);

        return UserResponseDTO.fromEntity(user);
    }

    public User findUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserTypeException("User not found"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
