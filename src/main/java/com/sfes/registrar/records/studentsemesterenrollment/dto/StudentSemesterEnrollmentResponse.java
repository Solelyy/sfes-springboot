package com.sfes.registrar.records.studentsemesterenrollment.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.Status;

import java.util.List;

public record StudentSemesterEnrollmentResponse(
        List<StudentSemesterEnrollmentDto> studentSemesterEnrollments,
        Meta meta
) {
    public record StudentSemesterEnrollmentDto(
            Long id,
            String studentId,
            String studentName,
            String courseCode,
            int yearLevel,
            String sectionName,
            Status status
    ) {}
}
