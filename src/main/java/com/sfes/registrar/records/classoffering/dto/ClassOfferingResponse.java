package com.sfes.registrar.records.classoffering.dto;

import com.sfes.common.classes.Meta;

import java.util.List;

public record ClassOfferingResponse(
    List<ClassOfferingDto> classOfferings,
    Meta meta
) {
    public record ClassOfferingDto(
            Long id,
            String subjectCode,
            String subjectName
    ){}
}
