package com.sfes.registrar.course;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.yearlevel.YearLevel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "courses")
public class Course extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "course_seq",
            sequenceName = "course_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "course_name", nullable = false, unique = true)
    private String courseName;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "duration_years", nullable = false)
    private int durationYears;

    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.PERSIST
    )
    private List<YearLevel> yearLevels = new ArrayList<>();
}
