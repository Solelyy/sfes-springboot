package com.sfes.registrar.yearlevel;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import com.sfes.registrar.course.Course;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "year_levels",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_year_level_course",
                        columnNames = {"year_number", "course_id"}
                )
        })
public class YearLevel extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "yr_lvl_seq",
            sequenceName = "yr_lvl_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yr_lvl_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "year_number", nullable = false)
    private int yearNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
