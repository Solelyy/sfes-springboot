package com.sfes.registrar.faculty.dto;

import java.util.List;

public record FacultySearchResponse (
        List<SearchResultItem> facultyMembers
){
    public record SearchResultItem(
            Long id,
            String firstName,
            String middleName,
            String lastName,
            boolean isAssigned
    ){}

    public record AssignedFaculty(
            Long id,
            String firstName,
            String middleName,
            String lastName
    ){}
}
