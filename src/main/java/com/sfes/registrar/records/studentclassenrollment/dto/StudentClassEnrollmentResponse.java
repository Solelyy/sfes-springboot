package com.sfes.registrar.records.studentclassenrollment.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.Status;

import java.util.List;

public record StudentClassEnrollmentResponse (
        List<StudentClassEnrollmentDto> studentClassEnrollments,
        Meta meta
) {
    public record StudentClassEnrollmentDto(
            Long studentId,
            String studentIdNumber,
            String studentFullName,
            String sectionName,
            Status status
    ){}
}
