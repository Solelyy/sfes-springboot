package com.sfes.registrar.faculty.service;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.faculty.Faculty;
import com.sfes.registrar.faculty.FacultyRepository;
import com.sfes.registrar.faculty.dto.FacultyResponse;
import com.sfes.registrar.faculty.dto.RegisterFacultyRequest;
import com.sfes.registrar.faculty.dto.UpdateFacultyRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void addFacultyMember(RegisterFacultyRequest request) {
        String normalizedEmployeeId = NormalizationUtil.normalizeToEmployeeId(request.employeeId());
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(request.email());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        if (facultyRepository.findByEmployeeId(normalizedEmployeeId).isPresent()) {
            throw new InvalidRequestException("Employee ID already exists");
        }

        if (facultyRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new InvalidRequestException("Email already exists");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        Faculty faculty = Faculty.builder()
                .employeeId(normalizedEmployeeId)
                .email(normalizedEmail)
                .firstName(request.firstName())
                .middleName(request.middleName())
                .lastName(request.lastName())
                .status(Status.ACTIVE)
                .department(department)
                .build();

        facultyRepository.save(faculty);
    }

    public FacultyResponse getFaculty(){
        List<FacultyResponse.FacultyDto> facultyList =
                facultyRepository.findAll()
                        .stream()
                        .map((faculty) -> new FacultyResponse.FacultyDto(
                                faculty.getId(),
                                faculty.getEmployeeId(),
                                faculty.getEmail(),
                                faculty.getFirstName(),
                                faculty.getMiddleName(),
                                faculty.getLastName(),
                                faculty.getDepartment().getDepartmentCode(),
                                faculty.getStatus()
                        ))
                        .toList();

        return new FacultyResponse(facultyList);
    }

    @Transactional
    public void updateFaculty(Long id, UpdateFacultyRequest request) {
        String normalizedEmployeeId = NormalizationUtil.normalizeToEmployeeId(request.employeeId());
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(request.email());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Faculty member does not exist"));

        if (facultyRepository
                .findByEmployeeIdAndIdNot(normalizedEmployeeId, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Employee ID already exists");
        }

        if (facultyRepository
                .findByEmailAndIdNot(normalizedEmail, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Email already exists");
        }

        Department department = departmentRepository
                .findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        faculty.setEmployeeId(normalizedEmployeeId);
        faculty.setEmail(normalizedEmail);
        faculty.setFirstName(request.firstName());
        faculty.setMiddleName(request.middleName());
        faculty.setLastName(request.lastName());
        faculty.setDepartment(department);
        faculty.setStatus(request.status());
    }
}
