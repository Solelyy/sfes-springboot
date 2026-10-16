package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.classschedule.ClassSchedule;
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
@Table
public class StudentClassEnrollment extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "student_enrollment_seq",
            sequenceName = "student_enrollment_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_enrollment_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_schedule_id", nullable = false)
    private ClassSchedule classSchedule;

    @Column(name = "enrolled_at", nullable = false)
    private Instant enrolledAt;

    @Column(name = "withdrawn_at")
    private Instant withdrawnAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
