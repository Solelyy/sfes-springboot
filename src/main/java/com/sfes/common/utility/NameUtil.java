
package com.sfes.common.utility;

public class NameUtil {

    private NameUtil() {}

    public static String fullName(
            String firstName,
            String lastName,
            String middleName
    ) {
        String first = normalize(firstName);
        String last = normalize(lastName);
        String middle = normalize(middleName);

        if (middle.isEmpty()) {
            return "%s, %s".formatted(last, first);
        }

        String middleInitials = getMiddleInitials(middle);

        return "%s, %s %s".formatted(last, first, middleInitials);
    }

    private static String getMiddleInitials(String middleName) {
        return java.util.Arrays.stream(middleName.split("\\s+"))
                .filter(part -> !part.isBlank())
                .map(part -> part.substring(0, 1).toUpperCase() + ".")
                .collect(java.util.stream.Collectors.joining(" "));
    }

    private static String normalize(String name) {
        return name == null ? "" : name.trim().replaceAll("\\s+", " ");
    }
}
