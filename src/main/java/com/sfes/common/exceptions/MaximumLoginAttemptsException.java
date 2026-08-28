package com.sfes.common.exceptions;

public class MaximumLoginAttemptsException extends RuntimeException {
    public MaximumLoginAttemptsException(String message) {
        super(message);
    }
}

