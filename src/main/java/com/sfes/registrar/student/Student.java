package com.sfes.registrar.student;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.department.Department;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "students")
public class Student extends BaseEntity {
    @Column(name = "student_id", unique = true, nullable = false)
    private String studentId;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "first_name", nullable = false, length = 100)
    private  String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
