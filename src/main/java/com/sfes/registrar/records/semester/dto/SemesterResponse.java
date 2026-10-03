package com.sfes.registrar.records.semester.dto;

import com.sfes.registrar.records.semester.SemesterType;

import java.util.List;

public record SemesterResponse (
        List<SemesterDto> semesters
){
    public record SemesterDto(Long id, SemesterType semesterType){}
}
