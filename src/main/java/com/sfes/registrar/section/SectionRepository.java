package com.sfes.registrar.section;

import com.sfes.registrar.section.dto.SectionSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

    @Query("""
    SELECT new com.sfes.registrar.section.dto.SectionSearchResult$SearchResultItem(
        s.id,
        s.sectionName,
        CASE WHEN EXISTS (
            SELECT css
            FROM ClassScheduleSection css
            WHERE css.section.id = s.id
               AND css.classSchedule.id = :classScheduleId
        ) THEN true ELSE false END
    ) 
    FROM Section s
    ORDER BY s.sectionName ASC, s.id ASC
    """)
    List<SectionSearchResponse.SearchResultItem> initialSectionSearch(
            @Param("classScheduleId") Long classScheduleId,
            Pageable pageable
    );


    @Query("""
    SELECT new com.sfes.registrar.section.dto.SectionSearchResult$SearchResultItem(
        s.id,
        s.sectionName,
        CASE WHEN EXISTS (
            SELECT css
            FROM ClassScheduleSection css
            WHERE css.section.id = s.id
               AND css.classSchedule.id = :classScheduleId
        ) THEN true ELSE false END
    ) 
    FROM Section s
    WHERE LOWER(s.sectionName) LIKE LOWER(CONCAT('%', :searchItem, '%'))
    ORDER BY s.sectionName ASC, s.id ASC
    """)
    List<SectionSearchResponse.SearchResultItem> findSectionSearchResult(
            @Param("classScheduleId") Long classScheduleId,
            @Param("searchItem") String searchItem,
            Pageable pageable
    );
}
