package com.sfes.registrar.records.schoolyear.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SchoolYearRequest (
        @NotBlank
        @Size(max = 25)
        String schoolYear
)
{}
