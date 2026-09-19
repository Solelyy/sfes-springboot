package com.sfes.registrar.faculty;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByEmployeeId(String employeeId);

    Optional<Faculty> findByEmail(String email);

    Optional<Faculty> findByEmployeeIdAndIdNot(String employeeId, Long id);

    Optional<Faculty> findByEmailAndIdNot(String email, Long id);

    @Query("""
        SELECT f 
        FROM Faculty f
        JOIN FETCH f.department d
        WHERE :departmentCode IS NULL OR d.departmentCode = :departmentCode
    """)
    Page<Faculty> findFaculty(
            @Param("departmentCode") String departmentCode,
            Pageable pageable
    );
}
