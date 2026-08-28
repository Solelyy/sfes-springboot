package com.sfes.superadmin.account.email;

import java.util.Map;

public record EmailContext(
        String from,
        String to,
        String subject,
        String template,
        Map<String, Object> variables
) {}
