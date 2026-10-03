package com.sfes.registrar.records.schoolyear.dto;

import java.util.List;

public record SchoolYearResponse(
        List<SchoolYearDto> schoolYears
)
{
    public record SchoolYearDto(Long id, String schoolYear){}
}
