package com.sfes.registrar.student.dto;

public record StudentSemEnrollmentSearchResponse(
        Long id,
        String studentId,
        String studentName,
        String courseCode,
        boolean isEnrolled
) {
}
