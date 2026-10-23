package com.sfes.registrar.subject.dto;

import java.util.List;

public record SubjectSearchResponse(
        List<SearchResultItem> searchResults
) {
    public record SearchResultItem(
            Long subjectId,
            String subjectName,
            String subjectCode,
            boolean isOffered
    ){}
}
