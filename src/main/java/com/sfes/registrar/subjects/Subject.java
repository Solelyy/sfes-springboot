package com.sfes.registrar.subjects;

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
@Table(name = "subjects")
public class Subject extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "subject_seq",
            sequenceName = "subject_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subject_seq")
    private Long id;

    @Column(name = "subject_name", unique = true, nullable = false)
    private String subjectName;

    @Column(name = "subject_code", unique = true, nullable = false, length = 25)
    private String subjectCode;

    @Column(name = "unit_count", nullable = false)
    private int unitCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
}
