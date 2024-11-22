package com.moreira.picpaychallenge.presentation.handlers;

import com.moreira.picpaychallenge.application.dto.ErrorResponse;
import com.moreira.picpaychallenge.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation Failed",
                errors.toString(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "VALIDATION_ERROR"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidNameException.class)
    public ResponseEntity<ErrorResponse> handleParameterNotFilledException(InvalidNameException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "INVALID_NAME"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterNotFilledException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(ParameterNotFilledException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "PARAMETER_NOT_FILLED"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(EmailNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "EMAIL_ALREADY_USED"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DocumentAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(DocumentAlreadyUsedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "DOCUMENT_ALREADY_USED"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(InvalidPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "INVALID_PASSWORD"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(InsufficientBalanceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "INSUFFICIENT_BALANCE"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserTypeException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(UserTypeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "USER_TYPE_ERROR"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidTransactionAmountException.class)
    public ResponseEntity<ErrorResponse> handleEmptyParameterException(InvalidTransactionAmountException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "TRANSACTION_AMOUNT_ERROR"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid UserType value provided. Allowed values: MERCHANT, COMMON",
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "INVALID_ENUM_VALUE"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
