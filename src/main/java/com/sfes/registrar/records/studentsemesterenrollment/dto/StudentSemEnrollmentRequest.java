package com.sfes.registrar.records.studentsemesterenrollment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudentSemEnrollmentRequest(
        @NotNull(message = "Student ID is required")
        @Min(value = 1, message = "Student ID must be a positive number")
        @Max(value = Long.MAX_VALUE, message = "Student ID must be a valid long value")
        Long studentId,

        @Max(value = Long.MAX_VALUE, message = "Section ID must be a valid long value")
        Long sectionId
) {
}
