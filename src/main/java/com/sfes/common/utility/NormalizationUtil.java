package com.sfes.common.utility;

import com.sfes.common.exceptions.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@Slf4j
public final class NormalizationUtil {
    private NormalizationUtil() {}

    private static final String EMPLOYEE_ID_PREFIX = "EMP";

    public static String normalizeToUpperCase(String text){
        if (text == null) return  null;

        return text.trim().toUpperCase(Locale.ROOT);
    }

    public static String normalizeToLowerCase(String text){
        if (text == null) return  null;

        return text.trim().toLowerCase(Locale.ROOT);
    }

    public static String normalizeToEmployeeId(String text) {
        if (text == null) return  null;

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
        if (name == null) return null;

        return name.trim().replaceAll("\\s+", " ");
    }

    public static String normalizeStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new InvalidRequestException("Student ID is required");
        }

        log.debug("Student ID: {}", studentId);

        String normalized = studentId.trim()
                .replaceAll("[\\u2010\\u2011\\u2012\\u2013\\u2014\\u2015\\u2212\\uFF0D]", "-");

        log.debug("Normalized Student ID: {}", normalized);

        if (!normalized.matches("\\d{2}-?\\d+")) {
            throw new InvalidRequestException("Invalid student ID format");
        }

        if (!normalized.contains("-")) {
            normalized = normalized.substring(0, 2) + "-" + normalized.substring(2);
        }

        return normalized;
    }

    public static String normalizeStudentIdSafe(String studentId) {
        try {
            return normalizeStudentId(studentId);
        } catch (InvalidRequestException e) {
            return null;
        }
    }
}
