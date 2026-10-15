package com.sfes.registrar.records.facultyassignment;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.faculty.Faculty;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "faculty_assignments")
public class FacultyAssignment extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "faculty_assignment_seq",
            sequenceName = "faculty_assignment_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "faculty_assignment_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_schedule_id", nullable = false)
    private ClassSchedule classSchedule;

    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;

    @Column(name = "unassigned_at")
    private Instant unassignedAt;
}
