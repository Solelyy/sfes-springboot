package com.sfes.registrar.yearlevel.dto;

import java.util.List;

public record YearLevelResponse(
    List<YearLevelDto> yearLevels
) {
    public record YearLevelDto(
            Long id,
            int yearNumber
    ){}
}
