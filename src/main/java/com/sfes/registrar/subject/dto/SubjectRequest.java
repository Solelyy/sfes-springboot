package com.sfes.registrar.subject.dto;

import jakarta.validation.constraints.*;

public record SubjectRequest(
        @NotBlank(message = "Subject name is required")
        @Size(min = 5, max = 255, message = "Subject name must be between 5 and 255 characters")
        String subjectName,

        @NotBlank(message = "Subject code is required")
        @Size(min = 2, max = 255, message = "Subject code must be between 2 and 255 characters")
        String subjectCode,

        @Min(value = 2, message = "Unit count must be at least 2")
        @NotNull(message = "Unit count is required")
        @Max(value = 4, message = "Unit count must not exceed 4")
        int unitCount,

        @NotBlank(message = "Course Code is required")
        @Size(min = 1, max = 25, message = "Course Code must be between 1 and 25 characters")
        String courseCode
) {}
