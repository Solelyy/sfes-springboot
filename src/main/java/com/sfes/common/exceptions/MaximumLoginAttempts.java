package com.sfes.common.exceptions;

public class MaximumLoginAttempts extends RuntimeException {
    public MaximumLoginAttempts(String message) {
        super(message);
    }
}

