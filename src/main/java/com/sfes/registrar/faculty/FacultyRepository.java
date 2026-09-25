package com.sfes.registrar.faculty;

import com.sfes.registrar.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
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
}
