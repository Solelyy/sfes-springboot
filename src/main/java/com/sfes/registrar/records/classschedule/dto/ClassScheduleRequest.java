package com.sfes.registrar.records.classschedule.dto;

import com.sfes.registrar.records.classschedule.DayOfWeek;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record ClassScheduleRequest (
        @NotNull
        DayOfWeek dayOfWeek,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime,

        @NotBlank
        @Size(min = 3, max = 25)
        String room
){}
