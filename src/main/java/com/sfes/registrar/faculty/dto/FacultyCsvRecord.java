package com.sfes.registrar.faculty.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FacultyCsvRecord {
    @CsvBindByName(column = "Employee ID")
    private String employeeId;

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
