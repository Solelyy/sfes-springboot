package com.sfes.registrar.records.classschedule;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.records.classoffering.ClassOffering;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "class_schedules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_class_schedule_exact",
                        columnNames = {
                                "day_of_week",
                                "start_time",
                                "end_time",
                                "room",
                                "class_offering_id"
                        }
                )
        }
)
public class ClassSchedule extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "class_sched_seq",
            sequenceName = "class_sched_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_sched_seq")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "room", nullable = false, length = 25)
    private String room;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_offering_id", nullable = false)
    private ClassOffering classOffering;
}
