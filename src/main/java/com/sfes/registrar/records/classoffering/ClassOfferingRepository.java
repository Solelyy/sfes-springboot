package com.sfes.registrar.records.classoffering;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClassOfferingRepository extends JpaRepository<ClassOffering, Long> {
    Optional<ClassOffering> findBySubjectIdAndSemesterId(Long subjectId, Long semesterId);

    @Query("""
    SELECT co
    FROM ClassOffering co
    JOIN FETCH co.subject s
    JOIN s.course c
    WHERE (:courseCode IS NULL or c.courseCode = :courseCode)
    AND co.semester.id = :semesterId
    """)
    Page<ClassOffering> findClassOfferings(
            @Param("courseCode") String courseCode,
            @Param("semesterId") Long semesterId,
            Pageable pageable
    );
}
