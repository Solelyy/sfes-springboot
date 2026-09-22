package com.sfes.registrar.student.dto;

import com.sfes.common.classes.Meta;
import com.sfes.registrar.student.StudentStatus;

import java.util.List;

public record StudentResponse(
        List<StudentDTO> records,
        Meta meta
) {
    public record StudentDTO(
            Long id,
            String studentId,
            String email,
            String firstName,
            String middleName,
            String lastName,
            String departmentCode,
            StudentStatus status
    ){}
}
