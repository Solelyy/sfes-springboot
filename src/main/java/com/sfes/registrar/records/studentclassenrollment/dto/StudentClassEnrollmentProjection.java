package com.sfes.registrar.records.studentclassenrollment.dto;

import com.sfes.registrar.Status;

public interface StudentClassEnrollmentProjection {

    Long getStudentId();

    String getStudentIdNumber();

    String getFirstName();

    String getMiddleName();

    String getLastName();

    String getSectionName();

    Status getStatus();
}