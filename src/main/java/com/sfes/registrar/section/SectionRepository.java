package com.sfes.registrar.section;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findBySectionNameAndYearLevelId(String sectionName, Long yearLevelId);

    @Query("""
        SELECT s
        FROM Section s
        WHERE (s.yearLevel.course.courseCode = :courseCode) 
        AND (s.yearLevel.yearNumber = :yearNumber )
    """)
    Page<Section> findSections(
            @Param("courseCode") String courseCode,
            @Param("yearNumber") int yearNumber,
            Pageable pageable
    );

    Optional<Section> findBySectionNameAndYearLevelIdAndIdNot(
            String sectionName,
            Long yearLevelId,
            Long id
    );
}
