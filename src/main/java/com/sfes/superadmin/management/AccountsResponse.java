package com.sfes.superadmin.management;

import com.sfes.user.enums.Role;
import com.sfes.user.enums.Status;

import java.util.List;

public record AccountsResponse(
        List<Account> accounts
) {
    public record Account(
            String employeeId,
            String email,
            String firstName,
            String middleName,
            String lastName,
            Role role,
            Status status
    ) {}
}
