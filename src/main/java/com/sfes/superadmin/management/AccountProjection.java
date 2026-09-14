package com.sfes.superadmin.management;

import com.sfes.user.enums.Role;
import com.sfes.user.enums.Status;

import java.time.Instant;

public interface AccountProjection {
    String getEmployeeId();
    String getEmail();
    String getFirstName();
    String getMiddleName();
    String getLastName();
    Role getRole();
    Status getStatus();
    Instant getExpiresAt();
}
