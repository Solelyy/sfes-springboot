package com.sfes.registrar.section.dto;

import com.sfes.common.classes.Meta;

import java.util.List;

public record SectionResponse (
        List<SectionDto> sections,
        Meta meta
) {
    public record SectionDto(
            Long id,
            String sectionName,
            int yearNumber,
            String courseCode
    ){}
}
