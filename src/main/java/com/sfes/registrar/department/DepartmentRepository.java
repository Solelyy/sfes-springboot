package com.sfes.registrar.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByDepartmentName(String departmentName);
    Optional<Department> findByDepartmentCode(String departmentCode);

    List<Department> findByDepartmentCodeIn(Collection<String> departmentCode);
}
