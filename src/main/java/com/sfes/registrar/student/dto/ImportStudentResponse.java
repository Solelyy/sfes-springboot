package com.sfes.registrar.student.dto;

import java.util.List;

public record ImportStudentResponse(
        int totalCount,
        int successfulCount,
        int failedCount,
        List<FailedRow> failedRows
) {
    public record FailedRow(
            String studentId,
            String email,
            String firstname,
            String middleName,
            String lastName,
            String courseCode,
            List<String> reasons
    ){}
}
