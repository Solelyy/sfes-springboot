package com.sfes.registrar.section.dto;

import java.util.List;

public record SectionSearchResult(
        List<SearchResultItem> searchResults
) {
    public record SearchResultItem(
            Long sectionId,
            String sectionName,
            boolean isAssigned
    ){}
}
