package com.sfes.registrar.subject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectRequest(
        @NotBlank
        @Size(min = 5, max = 255)
        String subjectName,

        @NotBlank
        @Size(min = 2, max = 255)
        String subjectCode,

        @Min(2)
        @Max(4)
        int unitCount,

        @NotBlank
        @Size(max = 25)
        String departmentCode
) {}
