package com.sfes.registrar.course.dto;

import jakarta.validation.constraints.*;

public record CreateCourse(
        @NotBlank
        @Size(max = 100)
        String courseName,

        @NotBlank
        @Size(max = 25)
        String departmentCode,

        @NotBlank
        @Size(max = 25)
        String courseCode
)
{}
