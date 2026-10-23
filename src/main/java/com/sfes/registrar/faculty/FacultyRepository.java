package com.sfes.registrar.faculty;

import com.sfes.registrar.Status;
import com.sfes.registrar.faculty.dto.FacultySearchProjection;
import com.sfes.registrar.faculty.dto.FacultySearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByEmployeeId(String employeeId);

    Optional<Faculty> findByEmail(String email);

    Optional<Faculty> findByEmployeeIdAndIdNot(String employeeId, Long id);

    Optional<Faculty> findByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT f 
        FROM Faculty f
        JOIN FETCH f.department d
        WHERE (:departmentCode IS NULL OR d.departmentCode = :departmentCode)
        AND (:status IS NULL or f.status = :status)
    """)
    Page<Faculty> findFaculty(
            @Param("departmentCode") String departmentCode,
            @Param("status") Status status,
            Pageable pageable
    );

    @Query("""
        SELECT f.employeeId 
        FROM Faculty f
        WHERE f.employeeId in :employeeIds
    """)
    Set<String> findExistingEmployeeIds(@Param("employeeIds") Collection<String> employeeIds);

    @Query("""
        SELECT f.email
        FROM Faculty f
        WHERE f.email in :emails
    """)
    Set<String> findExistingEmails(@Param("emails") Collection<String> emails);

    @Query("""
    SELECT new com.sfes.registrar.faculty.dto.FacultySearchResponse$SearchResultItem(
        f.id,
        f.firstName,
        f.middleName,
        f.lastName,
        CASE WHEN EXISTS(
            SELECT fa
            FROM FacultyAssignment fa
            WHERE fa.faculty.id = f.id
                AND fa.classSchedule.id = :classScheduleId
            ) THEN true ELSE false END
        )
    FROM Faculty f
    ORDER BY f.lastName ASC, f.id ASC
    """)
    List<FacultySearchResponse.SearchResultItem> initialFacultySearch(
            @Param("classScheduleId") Long classScheduleId,
            Pageable pageable
    );

    @Query(value = """
    SELECT
        f.id AS id,
        f.first_name AS "firstName",
        f.middle_name AS "middleName",
        f.last_name AS "lastName",
        EXISTS (
            SELECT 1
            FROM faculty_assignments fa
            WHERE fa.faculty_id = f.id
              AND fa.class_schedule_id = :classScheduleId
        ) AS "isAssigned"
    FROM faculty f
    WHERE NOT EXISTS (
        SELECT 1
        FROM unnest(
            regexp_split_to_array(
                LOWER(TRIM(:searchItem)),
                '\\s+'
            )
        ) AS word
        WHERE word <> ''
          AND LOWER(
              CONCAT_WS(
                  ' ',
                  f.first_name,
                  f.middle_name,
                  f.last_name
              )
          ) !~ ('\\y' || word || '\\y')
    )
    ORDER BY f.last_name ASC, f.id ASC
    """, nativeQuery = true)
    List<FacultySearchProjection> findFacultySearch(
            @Param("classScheduleId") Long classScheduleId,
            @Param("searchItem") String searchItem,
            Pageable pageable
    );
}
