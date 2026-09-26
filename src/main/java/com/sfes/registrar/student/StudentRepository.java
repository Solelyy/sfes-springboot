package com.sfes.registrar.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional <Student> findByStudentId(String studentId);

    Optional <Student> findByEmail(String email);

    Optional <Student> findByStudentIdAndIdNot(String studentId, Long id);

    Optional <Student> findByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT s 
        FROM Student s
        JOIN FETCH s.department d
        WHERE (:departmentCode IS NULL or d.departmentCode = :departmentCode)
        AND (:status IS NULL or s.status = :status)
    """)
    Page<Student> findStudents(
            @Param("departmentCode") String departmentCode,
            @Param("status") StudentStatus status,
            Pageable pageable
    );

    @Query("""
        SELECT s.studentId 
        FROM Student s
        WHERE s.studentId in :studentIds
    """)
    Set<String> findExistingStudentIds(@Param("studentIds") Collection<String> studentIds);

    @Query("""
        SELECT s.email
        FROM Student s
        WHERE s.email in :emails
    """)
    Set<String> findExistingEmails(@Param("emails") Collection<String> emails);
}
