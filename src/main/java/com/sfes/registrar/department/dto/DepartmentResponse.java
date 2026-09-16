package com.sfes.registrar.department.dto;

import com.sfes.registrar.Status;

import java.util.List;

public record DepartmentResponse (
        List<DepartmentDto> departments
){
    public record DepartmentDto(
            String departmentName,
            String departmentCode,
            Status status
    ){}
}
