package com.sfes.common.utility;

import com.sfes.common.exceptions.PasswordMismatchException;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    public void checkPasswordsMatch(String password, String confirmPassword){
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Passwords do not match");
        }
    }
}
