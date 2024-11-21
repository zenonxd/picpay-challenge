package com.moreira.picpaychallenge.application.dto;

import com.moreira.picpaychallenge.domain.entities.User;
import com.moreira.picpaychallenge.domain.enums.UserType;

import java.math.BigDecimal;

public record UserResponseDTO(Long id,
                              String firstName,
                              String lastName,
                              String document,
                              String email,
                              BigDecimal balance,
                              UserType userType){

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getDocument(),
                user.getEmail(),
                user.getBalance(),
                user.getUserType()
        );
    }
}
