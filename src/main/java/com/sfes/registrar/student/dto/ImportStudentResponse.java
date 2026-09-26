package com.sfes.registrar.student.dto;

import java.util.List;

public record ImportStudentResponse(
        int totalCount,
        int successfulCount,
        int failedCount,
        List<FailedRow> failedRows
) {
    public record FailedRow(
            int rowNumber,
            String studentId,
            List<String> reasons
    ){}
}
