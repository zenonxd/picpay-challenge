package com.moreira.picpaychallenge.application.dto;

import com.moreira.picpaychallenge.domain.enums.UserType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UserRequestDTO(
                            @NotBlank
                            @Size(min = 2)
                            String firstName,

                            @NotBlank
                            String lastName,

                            @NotBlank
                            String document,
                            @NotBlank
                            @Email
                            String email,
                            @NotBlank
                            @Size(min = 6)
                            String password,

                            @PositiveOrZero
                            BigDecimal balance,
                            UserType userType) {
}
