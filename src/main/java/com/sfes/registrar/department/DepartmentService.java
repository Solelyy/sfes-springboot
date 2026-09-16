package com.sfes.registrar.department;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
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
        String normalizedDeptName = NormalizationUtil.normalizeToUpperCase(departmentName);
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(departmentCode);

        if (departmentRepository.findByDepartmentName(normalizedDeptName).isPresent()) {
            throw new InvalidRequestException("Department name already exists");
        }

        if (departmentRepository.findByDepartmentCode(normalizedDeptCode).isPresent()) {
            throw new InvalidRequestException("Department code already exists");
        }

        Department department = Department.builder()
                .departmentName(normalizedDeptName)
                .departmentCode(normalizedDeptCode)
                .status(Status.ACTIVE)
                .build();

        departmentRepository.save(department);
    }

    @Transactional
    public void updateDeptStatus(String deptCode, Status status) {
        Department department = departmentRepository.findByDepartmentCode(deptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        if (department.getStatus() == status) {
            throw new InvalidRequestException("Unable to change to current status");
        }

        department.setStatus(status);
    }

    public DepartmentResponse getDepartments() {
        List<DepartmentResponse.DepartmentDto> departments =
                departmentRepository.findAll()
                        .stream()
                        .map(dept -> new DepartmentResponse.DepartmentDto(
                                dept.getDepartmentName(),
                                dept.getDepartmentCode(),
                                dept.getStatus()
                        ))
                        .toList();

        return new DepartmentResponse(departments);
    }
}

