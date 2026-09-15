package com.sfes.registrar.department;

import com.sfes.common.classes.BaseEntity;
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
    @Column(name = "dept_name", unique = true, length = 100)
    private String departmentName;

    @Column(name = "dept_code", unique = true, length = 25)
    private String departmentCode;

    @Column(name = "dept_status")
    @Enumerated(EnumType.STRING)
    private DepartmentStatus departmentStatus;
}
