package com.sfes.common.exceptions;

public class InvalidAccountRoleException extends RuntimeException {
    public InvalidAccountRoleException(String message) {
        super(message);
    }
}
