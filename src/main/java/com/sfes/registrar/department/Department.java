package com.sfes.registrar.department;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "departments_seq",
            sequenceName = "departments_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departments_seq")
    private Long id;

    @Column(name = "dept_name", unique = true, length = 100)
    private String departmentName;

    @Column(name = "dept_code", unique = true, length = 25)
    private String departmentCode;

    @Column(name = "dept_status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
