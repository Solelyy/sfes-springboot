package com.sfes.common.utility;

import java.util.Locale;

public final class NormalizationUtil {
    private NormalizationUtil() {}

    private static final String EMPLOYEE_ID_PREFIX = "EMP";

    public static String normalizeToUpperCase(String text){
        return text.trim().toUpperCase(Locale.ROOT);
    }

    public static String normalizeToLowerCase(String text){
        return text.trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizeToEmployeeId(String text) {
        String normalized = text.trim().toUpperCase(Locale.ROOT);

        if (normalized.startsWith(EMPLOYEE_ID_PREFIX)) {
            normalized = normalized.substring(EMPLOYEE_ID_PREFIX.length());
        }

        if (normalized.startsWith("-")) {
            normalized = normalized.substring(1);
        }

        return EMPLOYEE_ID_PREFIX + "-" + normalized;
    }
}
