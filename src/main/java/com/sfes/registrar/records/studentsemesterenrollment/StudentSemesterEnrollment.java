package com.sfes.registrar.records.studentsemesterenrollment;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.semester.Semester;
import com.sfes.registrar.section.Section;
import com.sfes.registrar.student.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "student_semester_enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_student_semester_enrollment",
                        columnNames = {"student_id", "semester_id"}
                )
        }
)

public class StudentSemesterEnrollment extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "student_sem_enrollment_seq",
            sequenceName = "student_sem_enrollment_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sem_enrollment_seq")
    private Long id;

    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false, fetch= FetchType.LAZY)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @Column(name = "withdrawn_at")
    private Instant withdrawnAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
