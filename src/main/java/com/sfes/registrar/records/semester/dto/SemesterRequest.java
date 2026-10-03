package com.sfes.registrar.records.semester.dto;

import com.sfes.registrar.records.semester.SemesterType;
import jakarta.validation.constraints.NotNull;

public record SemesterRequest(
        @NotNull
        SemesterType semesterType
) {}
