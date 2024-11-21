package com.moreira.picpaychallenge.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransferResponseDTO(Long id,
                                  BigDecimal amount,
                                  LocalDateTime timestamp,
                                  Long senderId,
                                  Long receiverId) {
}
