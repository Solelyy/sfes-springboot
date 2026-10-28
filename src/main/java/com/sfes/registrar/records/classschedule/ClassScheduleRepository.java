package com.sfes.registrar.records.classschedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.Optional;

public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    Optional<ClassSchedule> findByDayOfWeekAndStartTimeAndEndTimeAndRoomAndClassOfferingId(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endtime,
            String room,
            Long classOfferingId
    );

    @Query("""
    SELECT cs
    FROM ClassSchedule cs
    JOIN FETCH cs.classOffering co
    JOIN FETCH co.subject sub
    WHERE co.id = :classOfferingId
    ORDER BY
        CASE
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
    Page<ClassSchedule> findClassSchedules(
            @Param("classOfferingId") Long classOfferingId,
            Pageable pageable
    );

    @Query("""
    SELECT cs.classOffering.semester.id
    FROM ClassSchedule cs
    WHERE cs.id = :classScheduleId
    """)
    Optional<Long> findSemesterIdByClassScheduleId(
            @Param("classScheduleId") Long classScheduleId
    );
}
