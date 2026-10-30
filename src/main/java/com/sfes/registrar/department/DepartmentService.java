package com.sfes.registrar.department;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.dto.DepartmentResponse;
import com.sfes.registrar.department.dto.UpdateDepartment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
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
    public void updateDeptStatus(Long id, UpdateDepartment request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        String normalizedDeptName = NormalizationUtil.normalizeToUpperCase(request.departmentName());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        if (departmentRepository.findByDepartmentNameAndIdNot(normalizedDeptName, id).isPresent()) {
            throw new InvalidRequestException("Department name already exists");
        }

        if(departmentRepository.findByDepartmentCodeAndIdNot(normalizedDeptCode, id).isPresent()) {
            throw new InvalidRequestException("Department code already exists");
        }

        department.setDepartmentName(normalizedDeptName);
        department.setDepartmentCode(normalizedDeptCode);
        department.setStatus(request.status());
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartments() {
        List<DepartmentResponse.DepartmentDto> departments =
                departmentRepository.findAll()
                        .stream()
                        .sorted(Comparator.comparing(Department::getId))
                        .map(dept -> new DepartmentResponse.DepartmentDto(
                                dept.getId(),
                                dept.getDepartmentName(),
                                dept.getDepartmentCode(),
                                dept.getStatus()
                        ))
                        .toList();

        return new DepartmentResponse(departments);
    }
}

