package com.moreira.picpaychallenge.application.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransferRequestDTO(
        BigDecimal value,

        Long senderId,

        Long receiverId) {
}
