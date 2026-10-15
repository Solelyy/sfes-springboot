package com.sfes.registrar.records.facultyassignment.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.records.classschedule.DayOfWeek;

import java.util.List;

public record FacultyAssignmentResponse (
        List<FacultyAssignmentDto> facultyAssignments,
        Meta meta
) {
    public record FacultyAssignmentDto(
            Long id,
            String professorName,
            String subjectCode,
            DayOfWeek dayOfWeek,
            String startTime,
            String endTime
    ){}
}
