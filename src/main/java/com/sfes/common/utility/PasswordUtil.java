package com.sfes.common.utility;

import com.sfes.common.exceptions.PasswordMismatchException;

public final class PasswordUtil {
    private PasswordUtil() {}

    public static void checkPasswordsMatch(
            String password,
            String confirmPassword
    ){
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }
}
