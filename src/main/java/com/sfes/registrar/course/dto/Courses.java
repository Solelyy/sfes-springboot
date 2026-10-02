package com.sfes.registrar.course.dto;

import java.util.List;

public record Courses(
        List<Course> courses
) {
    public record Course(
            Long id,
            String courseName,
            String courseCode,
            String departmentCode,
            int durationYears
    ) {}
}
