package com.sfes.registrar.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            @Param("departmentCode") String courseCode,
            Pageable pageable
    );

    Optional<Subject> findBySubjectNameAndIdNot(String subjectName, Long id);

    Optional<Subject> findBySubjectCodeAndIdNot(String subjectCode, Long id);
}
