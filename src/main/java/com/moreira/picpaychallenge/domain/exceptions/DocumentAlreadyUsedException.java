package com.moreira.picpaychallenge.domain.exceptions;

public class DocumentAlreadyUsedException extends RuntimeException {
    public DocumentAlreadyUsedException(String message) {
        super(message);
    }
}
