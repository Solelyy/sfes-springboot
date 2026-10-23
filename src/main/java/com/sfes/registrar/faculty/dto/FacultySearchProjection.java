package com.sfes.registrar.faculty.dto;

public interface FacultySearchProjection {
    Long getId();
    String getFirstName();
    String getMiddleName();
    String getLastName();
    boolean getIsAssigned();
}