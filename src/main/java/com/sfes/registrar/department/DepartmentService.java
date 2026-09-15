package com.sfes.registrar.department;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.registrar.department.dto.DepartmentResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void createDepartment(String departmentName, String departmentCode) {
        String sanitizedDeptName = departmentName.toUpperCase().trim();
        String sanitizedDeptCode = departmentCode.toUpperCase().trim();

        if (departmentRepository.findByDepartmentName(sanitizedDeptName).isPresent()) {
            throw new InvalidRequestException("Department name already exists");
        }

        if (departmentRepository.findByDepartmentCode(sanitizedDeptCode).isPresent()) {
            throw new InvalidRequestException("Department code already exists");
        }

        Department department = Department.builder()
                .departmentName(sanitizedDeptName)
                .departmentCode(sanitizedDeptCode)
                .departmentStatus(DepartmentStatus.ACTIVE)
                .build();

        departmentRepository.save(department);
    }

    @Transactional
    public void updateDeptStatus(String deptCode, DepartmentStatus departmentStatus) {
        Department department = departmentRepository.findByDepartmentCode(deptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        if (department.getDepartmentStatus() == departmentStatus) {
            throw new InvalidRequestException("Unable to change to current status");
        }

        department.setDepartmentStatus(departmentStatus);
    }

    public DepartmentResponse getDepartments() {
        List<DepartmentResponse.DepartmentDto> departments =
                departmentRepository.findAll()
                        .stream()
                        .map(dept -> new DepartmentResponse.DepartmentDto(
                                dept.getDepartmentName(),
                                dept.getDepartmentCode(),
                                dept.getDepartmentStatus()
                        ))
                        .toList();

        return new DepartmentResponse(departments);
    }
}

