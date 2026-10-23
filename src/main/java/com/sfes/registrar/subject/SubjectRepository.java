package com.sfes.registrar.subject;

import com.sfes.registrar.subject.dto.SubjectSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectName(String subjectName);

    Optional<Subject> findBySubjectCode(String subjectCode);

    @Query("""
        SELECT s 
        FROM Subject s
        JOIN FETCH s.course c
        WHERE (:courseCode IS NULL or c.courseCode = :courseCode)
    """)
    Page<Subject> findSubjects(
            @Param("courseCode") String courseCode,
            Pageable pageable
    );

    Optional<Subject> findBySubjectNameAndIdNot(String subjectName, Long id);

    Optional<Subject> findBySubjectCodeAndIdNot(String subjectCode, Long id);

    @Query("""
    SELECT new com.sfes.registrar.subject.dto.SubjectSearchResponse$SearchResultItem(
        s.id,
        s.subjectName,
        s.subjectCode,
        CASE WHEN EXISTS (
            SELECT co
            FROM ClassOffering co
            WHERE co.subject.id = s.id
              AND co.semester.id = :semesterId
        ) THEN true ELSE false END
    )
    FROM Subject s
    ORDER BY s.subjectCode ASC
    """)
    List<SubjectSearchResponse.SearchResultItem> initialSubjectsSearch(
            @Param("semesterId") Long semesterId,
            Pageable pageable
    );

    @Query("""
    SELECT new com.sfes.registrar.subject.dto.SubjectSearchResponse$SearchResultItem(
        s.id,
        s.subjectName,
        s.subjectCode,
        CASE WHEN EXISTS (
            SELECT co
            FROM ClassOffering co
            WHERE co.subject.id = s.id
              AND co.semester.id = :semesterId
        ) THEN true ELSE false END
    )
    FROM Subject s
    WHERE LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :searchItem, '%'))
        OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :searchItem, '%'))
    ORDER BY s.subjectCode ASC
    """)
    List<SubjectSearchResponse.SearchResultItem> findSubjectsSearchResult(
            @Param("semesterId") Long semesterId,
            @Param("searchItem") String searchItem,
            Pageable pageable
    );
}
