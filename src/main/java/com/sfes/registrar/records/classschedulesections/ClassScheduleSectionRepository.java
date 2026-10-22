package com.sfes.registrar.records.classschedulesections;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClassScheduleSectionRepository extends JpaRepository<ClassScheduleSection, Long> {
    Optional<ClassScheduleSection> findByClassScheduleIdAndSectionId(
            Long classScheduleId,
            Long sectionId
    );

    @Query("""
        SELECT css
        FROM ClassScheduleSection css
        JOIN FETCH css.section s
        WHERE css.classSchedule.id IN :classScheduleIds
    """)
    List<ClassScheduleSection> findAllByClassScheduleIdsWithSection(
            @Param("classScheduleIds") List<Long> classScheduleIds
    );
}
