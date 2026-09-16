package com.sfes.registrar.faculty;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Optional<Faculty> findByEmployeeId(String employeeId);

    Optional<Faculty> findByEmail(String email);

    Optional<Faculty> findByEmployeeIdAndIdNot(String employeeId, Long id);

    Optional<Faculty> findByEmailAndIdNot(String email, Long id);
}
