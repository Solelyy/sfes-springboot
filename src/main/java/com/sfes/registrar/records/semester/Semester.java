package com.sfes.registrar.records.semester;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.records.schoolyear.SchoolYear;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(
        name = "semesters",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_semester_school_year_type",
                        columnNames = {"school_year_id", "semester_type"}
                )
        })
public class Semester extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "semester_seq",
            sequenceName = "semester_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester_type", nullable = false)
    private SemesterType semesterType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;
}
