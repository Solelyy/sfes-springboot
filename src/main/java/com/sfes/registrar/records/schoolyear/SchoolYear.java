package com.sfes.registrar.records.schoolyear;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "school_years")
public class SchoolYear {
    @Id
    @SequenceGenerator(
            name = "schoolyr_seq",
            sequenceName = "schoolyr_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "schoolyr_seq")
    private Long id;

    @Column(name = "school_year", unique = true, nullable = false, length = 25)
    private String schoolYear;

}
