package com.sfes.registrar.faculty.dto;

import java.util.List;

public record FacultySearchResponse (
        List<SearchResultItem> facultyMembers
){
    public record SearchResultItem(
            Long id,
            String fullName
    ){}
}
