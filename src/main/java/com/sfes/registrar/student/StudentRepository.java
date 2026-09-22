package com.sfes.registrar.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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
}
