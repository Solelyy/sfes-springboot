package com.sfes.registrar.records.schoolyear.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SchoolYearRequest (
        @NotBlank(message = "School year is required")
        @Size(min = 1, max = 25, message = "School year must be between 1 and 25 characters")
        String schoolYear
)
{}
