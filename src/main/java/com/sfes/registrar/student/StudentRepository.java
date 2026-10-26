package com.sfes.registrar.student;

import com.sfes.registrar.Status;
import com.sfes.registrar.student.dto.StudentSemEnrollmentSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByStudentIdAndIdNot(String studentId, Long id);

    Optional<Student> findByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT s 
        FROM Student s
        JOIN FETCH s.course c
        WHERE (:departmentCode IS NULL or c.department.departmentCode = :departmentCode)
        AND (:courseCode IS NULL or c.courseCode = :courseCode)
        AND (:status IS NULL or s.status = :status)
    """)
    Page<Student> findStudents(
            @Param("departmentCode") String departmentCode,
            @Param("courseCode") String courseCode,
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

    @Query(value = """
    SELECT
        s.id AS id,
        s.student_id AS "studentId",
        CONCAT_WS(
            ' ',
            s.first_name,
            s.middle_name,
            s.last_name
        ) AS "studentName",
        c.course_code AS "courseCode",
        EXISTS (
            SELECT 1
            FROM student_semester_enrollments sse
            WHERE sse.student_id = s.id
              AND sse.semester_id = :semesterId
              AND sse.status = 'ACTIVE'
        ) AS "isEnrolled"
    FROM students s
    JOIN courses c ON c.id = s.course_id
    WHERE s.student_id = :studentId
    """, nativeQuery = true)
    Optional<StudentSemEnrollmentSearchResponse> findStudentWithEnrollmentStatus(
            @Param("studentId") String studentId,
            @Param("semesterId") Long semesterId
    );
}
