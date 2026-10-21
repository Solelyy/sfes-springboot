package com.sfes.registrar.faculty.dto;

import java.util.List;

public record ImportFacultyResponse (
        int totalCount,
        int successfulCount,
        int failedCount,
        List<FailedRow> failedRows
) {
    public record FailedRow(
            String employeeId,
            String email,
            String firstname,
            String middleName,
            String lastName,
            String departmentCode,
            List<String> reasons
    ){}
}
