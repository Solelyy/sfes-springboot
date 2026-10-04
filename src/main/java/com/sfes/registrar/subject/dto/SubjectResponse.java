package com.sfes.registrar.subject.dto;

import com.sfes.common.classes.Meta;

import java.util.List;

public record SubjectResponse(
        List<SubjectDto> subjects,
        Meta meta
) {
    public record SubjectDto(
            Long id,
            String subjectName,
            String subjectCode,
            int unitCount,
            String departmentCode
    ) {}
}
