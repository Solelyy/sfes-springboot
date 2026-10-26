package com.sfes.registrar.records.studentsemesterenrollment;

import com.sfes.registrar.Status;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemEnrollmentProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentSemesterEnrollmentRepository extends JpaRepository<StudentSemesterEnrollment, Long> {

    Optional<StudentSemesterEnrollment> findByStudentIdAndSemesterId(
            Long studentId,
            Long semesterId
    );

    @Query(
            value = """
        SELECT sse.id AS id,
               s.studentId AS studentId,
               s.firstName AS firstName,
               s.middleName AS middleName,
               s.lastName AS lastName,
               c.courseCode AS courseCode,
               sse.yearLevel AS yearLevel,
               sec.sectionName AS sectionName,
               sse.status AS status
        FROM StudentSemesterEnrollment sse
        JOIN sse.student s
        JOIN sse.semester sem
        LEFT JOIN sse.section sec
        LEFT JOIN sec.yearLevel yl
        LEFT JOIN yl.course c
        WHERE sem.id = :semesterId
          AND (
              :departmentCode IS NULL
              OR c.department.departmentCode = :departmentCode
          )
          AND (
              :courseCode IS NULL
              OR c.courseCode = :courseCode
          )
          AND (
              :sectionName IS NULL
              OR sec.sectionName = :sectionName
          )
          AND (
              :status IS NULL
              OR sse.status = :status
          )
        ORDER BY s.studentId ASC
        """,
            countQuery = """
        SELECT COUNT(sse.id)
        FROM StudentSemesterEnrollment sse
        JOIN sse.semester sem
        LEFT JOIN sse.section sec
        LEFT JOIN sec.yearLevel yl
        LEFT JOIN yl.course c
        WHERE sem.id = :semesterId
          AND (
              :departmentCode IS NULL
              OR c.department.departmentCode = :departmentCode
          )
          AND (
              :courseCode IS NULL
              OR c.courseCode = :courseCode
          )
          AND (
              :sectionName IS NULL
              OR sec.sectionName = :sectionName
          )
          AND (
              :status IS NULL
              OR sse.status = :status
          )
        """
    )
    Page<StudentSemEnrollmentProjection> findSemesterEnrollment(
            @Param("semesterId") Long semesterId,
            @Param("departmentCode") String departmentCode,
            @Param("courseCode") String courseCode,
            @Param("sectionName") String sectionName,
            @Param("status") Status status,
            Pageable pageable
    );


}
