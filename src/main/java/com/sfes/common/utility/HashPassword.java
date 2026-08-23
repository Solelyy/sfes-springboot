package com.sfes.common.utility;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Scanner;

public class HashPassword {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        System.out.print("Enter password: ");
        String plainPassword = scanner.next();

        String hashedPassword = passwordEncoder.encode(plainPassword);

        System.out.println(hashedPassword);
    }
}

