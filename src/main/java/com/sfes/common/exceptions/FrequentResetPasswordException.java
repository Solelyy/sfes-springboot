package com.sfes.common.exceptions;

public class FrequentResetPasswordException extends RuntimeException {
    public FrequentResetPasswordException(String message) {
        super(message);
    }
}
