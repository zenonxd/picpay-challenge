package com.moreira.picpaychallenge.application.dto;

import java.time.LocalDateTime;

public record ErrorResponse(String message,
                            String details,
                            int status,
                            LocalDateTime timestamp,
                            String errorCode) {
}
