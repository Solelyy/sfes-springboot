package com.sfes.registrar.records.schoolyear;

import com.sfes.common.classes.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "school_years")
public class SchoolYear extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "school_yr_seq",
            sequenceName = "school_yr_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "school_yr_seq")
    private Long id;

    @Column(name = "school_year", unique = true, nullable = false, length = 25)
    private String schoolYear;

}
