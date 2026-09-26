package com.sfes.registrar.student.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class StudentCsvRecord {
    @CsvBindByName(column = "Student ID")
    private String studentId;

    @CsvBindByName(column = "Email")
    private String email;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Middle Name")
    private String middleName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @CsvBindByName(column = "Department Code")
    private String departmentCode;
}
