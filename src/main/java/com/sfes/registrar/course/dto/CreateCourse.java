package com.sfes.registrar.course.dto;

import jakarta.validation.constraints.*;

public record CreateCourse(
        @NotBlank(message = "Course Name is required")
        @Size(min = 1, max = 100, message = "Course Name must be between 1 and 100 characters")
        String courseName,

        @NotBlank(message = "Department Code is required")
        @Size(min = 1, max = 25, message = "Department Code must be between 1 and 25 characters")
        String departmentCode,

        @NotBlank(message = "Course Code is required")
        @Size(min = 1, max = 25, message = "Course Code must be between 1 and 25 characters")
        String courseCode,

        @NotNull(message = "Duration in years is required")
        @Min(value = 3, message = "Duration in years must be at least 3")
        @Max(value = 4, message = "Duration in years must be at most 4")
        Integer durationYears
)
{}
