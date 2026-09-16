package com.sfes.registrar.faculty;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.Department;
import jakarta.persistence.*;

@Entity
@Table(name = "faculty")
public class Faculty extends BaseEntity {
    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private  String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @JoinColumn(name = "department_id")
    private Department department;
}
