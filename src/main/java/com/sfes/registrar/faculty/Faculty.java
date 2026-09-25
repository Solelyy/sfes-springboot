package com.sfes.registrar.faculty;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.Department;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "faculty")
public class Faculty extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "faculty_seq",
            sequenceName = "faculty_seq",
            allocationSize = 100
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_seq")
    private Long id;

    @Column(name = "employee_id", unique = true, nullable = false)
    private String employeeId;

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
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
