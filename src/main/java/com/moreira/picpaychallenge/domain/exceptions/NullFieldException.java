package com.moreira.picpaychallenge.domain.exceptions;

public class NullFieldException extends RuntimeException {
    public NullFieldException(String message) {
        super(message);
    }
}
