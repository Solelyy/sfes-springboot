package com.sfes.registrar.records.classschedulesections;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import com.sfes.registrar.section.Section;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "class_schedule_sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_class_schedule_section",
                        columnNames = {"class_schedule_id", "section_id"}
                )
        }
)
public class ClassScheduleSection extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "class_sched_section_seq",
            sequenceName = "class_sched_section_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_sched_section_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "class_schedule_id", nullable = false)
    private ClassSchedule classSchedule;

    @ManyToOne(optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;
}
