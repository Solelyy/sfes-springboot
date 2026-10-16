package com.sfes.registrar.records.studentclassenrollment.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.records.classschedule.DayOfWeek;

import java.util.List;

public record StudentClassEnrollmentResponse (
        List<StudentClassEnrollmentDto> studentClassEnrollments,
        Meta meta
) {
    public record StudentClassEnrollmentDto(
            Long enrollmentId,
            String studentId,
            String studentFullName,
            String subjectCode,
            DayOfWeek dayOfWeek,
            String startTime,
            String endTime
    ){}
}
