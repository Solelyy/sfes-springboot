package com.sfes.registrar.records.studentsemesterenrollment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StudentSemEnrollmentRequest(
        @NotNull
        Long studentId,

        @NotNull
        Long sectionId,

        @NotNull
        @Min(1)
        @Max(4)
        int yearLevel
) {
}
