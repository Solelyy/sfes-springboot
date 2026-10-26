package com.sfes.registrar.records.studentsemesterenrollment.dto;

import com.sfes.registrar.Status;

public interface StudentSemEnrollmentProjection {
    Long getId();
    String getStudentId();
    String getFirstName();
    String getMiddleName();
    String getLastName();
    String getCourseCode();
    int getYearLevel();
    String getSectionName();
    Status getStatus();
}

