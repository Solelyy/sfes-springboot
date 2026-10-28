package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.registrar.Status;
import com.sfes.registrar.records.studentclassenrollment.dto.StudentClassEnrollmentProjection;
import com.sfes.registrar.records.studentsemesterenrollment.StudentSemesterEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentClassEnrollmentRepository extends JpaRepository<StudentClassEnrollment, Long> {
    boolean existsByStudentIdAndClassScheduleIdAndStatus(
            Long studentId,
            Long classScheduleId,
            Status status
    );

    @Query(
            value = """
            SELECT
                s.id AS studentId,
                s.studentId AS studentIdNumber,
                s.firstName AS firstName,
                s.middleName AS middleName,
                s.lastName AS lastName,
                sec.sectionName AS sectionName,
                sse.status AS status
            FROM StudentSemesterEnrollment sse
            JOIN sse.student s
            JOIN sse.semester sem
            LEFT JOIN sse.section sec
            WHERE sse.status = com.sfes.registrar.Status.ACTIVE
              AND sem.id = :semesterId
              AND EXISTS (
                  SELECT css.id
                  FROM ClassScheduleSection css
                  WHERE css.classSchedule.id = :classScheduleId
                    AND css.section.id = sse.section.id
              )
            OR sse.status = com.sfes.registrar.Status.ACTIVE
              AND sem.id = :semesterId
              AND EXISTS (
                  SELECT sce.id
                  FROM StudentClassEnrollment sce
                  WHERE sce.classSchedule.id = :classScheduleId
                    AND sce.student.id = s.id
                    AND sce.status = com.sfes.registrar.Status.ACTIVE
              )
            ORDER BY s.studentId ASC
            """,
            countQuery = """
            SELECT COUNT(sse.id)
            FROM StudentSemesterEnrollment sse
            JOIN sse.semester sem
            WHERE sse.status = com.sfes.registrar.Status.ACTIVE
              AND sem.id = :semesterId
              AND EXISTS (
                  SELECT css.id
                  FROM ClassScheduleSection css
                  WHERE css.classSchedule.id = :classScheduleId
                    AND css.section.id = sse.section.id
              )
            OR sse.status = com.sfes.registrar.Status.ACTIVE
              AND sem.id = :semesterId
              AND EXISTS (
                  SELECT sce.id
                  FROM StudentClassEnrollment sce
                  WHERE sce.classSchedule.id = :classScheduleId
                    AND sce.student.id = sse.student.id
                    AND sce.status = com.sfes.registrar.Status.ACTIVE
              )
            """
    )
    Page<StudentClassEnrollmentProjection> findStudentClassEnrollments(
            @Param("classScheduleId") Long classScheduleId,
            @Param("semesterId") Long semesterId,
            Pageable pageable
    );
}
