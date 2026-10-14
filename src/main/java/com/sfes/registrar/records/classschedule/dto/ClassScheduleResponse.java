package com.sfes.registrar.records.classschedule.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.records.classschedule.DayOfWeek;

import java.time.LocalTime;
import java.util.List;

public record ClassScheduleResponse (
        List<ClassScheduleDto> classSchedules,
        Meta meta
) {
    public record ClassScheduleDto(
            Long id,
            DayOfWeek dayOfWeek,
            String startTime,
            String endTime,
            String room,
            String subjectName,
            String subjectCode
    ){}
}
