package com.sfes.registrar.records.semester.dto;

import com.sfes.registrar.records.semester.SemesterType;
import jakarta.validation.constraints.NotNull;

public record SemesterRequest(
        @NotNull(message = "Semester type is required")
        SemesterType semesterType
) {}
