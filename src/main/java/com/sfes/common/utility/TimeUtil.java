package com.sfes.common.utility;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("h:mma");

    private TimeUtil() {}

    public static String formatTime(LocalTime time) {
        return time == null
                ? null
                : time.format(TIME_FORMATTER);
    }

    public static LocalTime normalizeTime(LocalTime time){
        return time.withSecond(0)
                .withNano(0);
    }

    public static void validateStartEndTime(LocalTime startTime, LocalTime endTime) {
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
    }
}