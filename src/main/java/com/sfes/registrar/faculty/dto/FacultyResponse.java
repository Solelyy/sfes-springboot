package com.sfes.registrar.faculty.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.Status;

import java.util.List;

public record FacultyResponse(
        List<FacultyDto> records,
        Meta meta
) {
    public record FacultyDto(
            Long id,
            String employeeId,
            String email,
            String firstName,
            String middleName,
            String lastName,
            String departmentCode,
            Status status
    ) {}
}
