package com.sfes.registrar.section;

import com.sfes.common.classes.BaseEntity;
import com.sfes.registrar.yearlevel.YearLevel;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_section_year_level",
                        columnNames = {"section_name", "year_level_id"}
                )
        }
)
public class Section extends BaseEntity {
    @Id
    @SequenceGenerator(
            name = "section_seq",
            sequenceName = "section_seq",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "section_seq")
    private Long id;


    @Column(name = "section_name", nullable = false, length = 25)
    private String sectionName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "year_level_id", nullable = false)
    private YearLevel yearLevel;
}
