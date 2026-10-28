package com.sfes.registrar.records.studentsemesterenrollment.dto;

import jakarta.validation.constraints.NotNull;

public record StudentSemEnrollmentRequest(
        @NotNull
        Long studentId,

        Long sectionId
) {
}
