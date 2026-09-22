package com.sfes.common.utility;

import com.sfes.common.exceptions.InvalidRequestException;

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

    public static String formatName(String name){
        if (name == null || name.isBlank()  ) {
            return "";
        }

         String[] separatedName = name.split("\\s+");

         StringBuilder result = new StringBuilder();

         for(String n : separatedName) {
             if (n.isEmpty()) {
                 continue;
             }

             result.append(n.substring(0,1).toUpperCase(Locale.ROOT)
                     + n.substring(1).toLowerCase(Locale.ROOT)
             ).append(" ");
         }

         return result.toString().trim();
    }

    public static String normalizeName(String name) {
        return name.trim().replaceAll("\\+s", " ");
    }

    public static String normalizeStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new InvalidRequestException("Student ID is required");
        }

        String normalized = studentId.trim();

        if (!normalized.matches("\\d{2}-?\\d+")) {
            throw new InvalidRequestException("Invalid student ID format");
        }

        if (!normalized.contains("-")) {
            normalized = normalized.substring(0, 2) + "-" + normalized.substring(2);
        }

        return normalized;
    }
}
