package com.moreira.picpaychallenge.domain.exceptions;

public class ParameterNotFilledException extends RuntimeException {
    public ParameterNotFilledException(String message) {
        super(message);
    }
}
