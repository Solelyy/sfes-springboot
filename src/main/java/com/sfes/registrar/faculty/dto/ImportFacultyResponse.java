package com.sfes.registrar.faculty.dto;

import java.util.List;

public record ImportFacultyResponse (
        int totalCount,
        int successfulCount,
        int failedCount,
        List<FailedRow> failedRows
) {
    public record FailedRow(
            int rowNumber,
            String employeeId,
            List<String> reasons
    ){}
}
