package com.sfes.employee.repository;

import com.sfes.employee.entity.Employee;
import com.sfes.superadmin.management.AccountProjection;
import com.sfes.user.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmployeeId(String employeeId);
    @Query("""
    SELECT
        e.employeeId AS employeeId,
        u.email AS email,
        e.firstName AS firstName,
        e.middleName AS middleName,
        e.lastName AS lastName,
        u.role AS role,
        u.status AS status,
        ai.expiresAt AS expiresAt
    FROM Employee e
    JOIN e.user u
    LEFT JOIN AccountInvitation ai
        ON ai.user = u
        AND ai.createdAt = (
            SELECT MAX(ai2.createdAt)
            FROM AccountInvitation ai2
            WHERE ai2.user = u
        )
    WHERE u.role <> com.sfes.user.enums.Role.SUPER_ADMIN
      AND (:role IS NULL OR u.role = :role)
    ORDER BY e.createdAt DESC
""")
    List<AccountProjection> findAccounts(@Param("role") Role role);
}
