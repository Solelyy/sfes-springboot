package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.registrar.Status;
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

    @Query("""
    SELECT sce
    FROM StudentClassEnrollment sce
    JOIN FETCH sce.student student
    JOIN FETCH sce.classSchedule cs
    JOIN FETCH cs.classOffering co
    JOIN FETCH co.subject subject
    WHERE co.semester.id = :semesterId
      AND sce.status = com.sfes.registrar.Status.ACTIVE
    ORDER BY CASE
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.M THEN 1
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.T THEN 2
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.W THEN 3
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.TH THEN 4
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.F THEN 5
        WHEN cs.dayOfWeek =
            com.sfes.registrar.records.classschedule.DayOfWeek.S THEN 6
        ELSE 7
    END ASC,
    cs.startTime ASC,
    cs.endTime ASC,
    cs.id ASC
    """)
    Page<StudentClassEnrollment> findStudentClassEnrollments(
            @Param("semesterId") Long semesterId,
            Pageable pageable
    );
}
