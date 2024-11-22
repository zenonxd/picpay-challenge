package com.moreira.picpaychallenge.application.dto;

import com.moreira.picpaychallenge.domain.enums.UserType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record UserRequestDTO(

                            String firstName,


                            String lastName,

                            String document,
                            @Email
                            String email,

                            String password,

                            BigDecimal balance,
                            UserType userType) {
}
