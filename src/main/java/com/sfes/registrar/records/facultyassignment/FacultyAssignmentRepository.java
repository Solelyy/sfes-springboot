package com.sfes.registrar.records.facultyassignment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FacultyAssignmentRepository extends JpaRepository<FacultyAssignment, Long> {

    Optional<FacultyAssignment> findByClassScheduleIdAndUnassignedAtIsNull(
            Long classScheduleId
    );

    @Query("""
        SELECT fa
        FROM FacultyAssignment fa
        JOIN FETCH fa.faculty f
        JOIN FETCH fa.classSchedule cs
        JOIN FETCH cs.classOffering co
        JOIN FETCH co.subject s
        WHERE co.semester.id = :semesterId
        AND fa.unassignedAt IS NULL
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
        END ASC,
        cs.startTime ASC,
        cs.endTime ASC,
        cs.id ASC
    """)
    Page<FacultyAssignment> findFacultyAssignments(
            @Param("semesterId") Long semesterId,
            Pageable pageable
    );

    @Query("""
    SELECT fa
    FROM FacultyAssignment fa
    JOIN FETCH fa.faculty 
    WHERE fa.classSchedule.id IN :classScheduleIds
      AND fa.unassignedAt IS NULL
    """)
    List<FacultyAssignment> findActiveAssignmentsByClassScheduleIds(
            @Param("classScheduleIds") List<Long> classScheduleIds
    );
}
