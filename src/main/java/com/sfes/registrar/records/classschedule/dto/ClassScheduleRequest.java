package com.sfes.registrar.records.classschedule.dto;

import com.sfes.registrar.records.classschedule.DayOfWeek;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record ClassScheduleRequest (
        @NotNull(message = "Day of week is required")
        DayOfWeek dayOfWeek,

        @NotNull(message = "Start time is required")
        LocalTime startTime,

        @NotNull(message = "End time is required")
        LocalTime endTime,

        @NotBlank(message = "Room is required")
        @Size(min = 3, max = 25, message = "Room must be between 3 and 25 characters")
        String room
){}
