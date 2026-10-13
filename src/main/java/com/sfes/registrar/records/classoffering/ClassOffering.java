package com.sfes.registrar.records.classoffering;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.records.semester.Semester;
import com.sfes.registrar.subject.Subject;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "class_offerings",
        uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"subject_id", "semester_id"}
        )}
)
public class ClassOffering extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "class_offerings_seq",
            sequenceName = "class_offerings_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "class_offerings_seq")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;
}
