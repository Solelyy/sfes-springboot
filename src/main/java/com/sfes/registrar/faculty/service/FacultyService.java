package com.sfes.registrar.faculty.service;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.faculty.Faculty;
import com.sfes.registrar.faculty.FacultyRepository;
import com.sfes.registrar.faculty.SortBy;
import com.sfes.registrar.faculty.dto.FacultyResponse;
import com.sfes.registrar.faculty.dto.FacultySearchResponse;
import com.sfes.registrar.faculty.dto.RegisterFacultyRequest;
import com.sfes.registrar.faculty.dto.UpdateFacultyRequest;
import com.sfes.registrar.records.classschedule.ClassScheduleRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassScheduleRepository classScheduleRepository;

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

    public FacultyResponse getFaculty(String departmentCode, int pageNumber, int size, SortBy sortBy, Sort.Direction direction, Status status){
        Sort sort = Sort.by(direction, sortBy.getProperty()).and(Sort.by("id").ascending());

        Pageable pageable = PageRequest.of(pageNumber - 1, size, sort);

        Page<Faculty> result = facultyRepository.findFaculty(departmentCode, status, pageable);

        List<FacultyResponse.FacultyDto> facultyList = result.getContent()
                .stream()
                .map((faculty)-> new FacultyResponse.FacultyDto(
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

        return new FacultyResponse(
                facultyList,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
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

    @Transactional(readOnly = true)
    public FacultySearchResponse getFacultySearchResponse(Long classScheduleId, String searchItem){
        if (!classScheduleRepository.existsById(classScheduleId)) {
            throw new InvalidRequestException("Class schedule not found");
        }

        String normalizedSearch = (searchItem == null || searchItem.isBlank())
                ? null
                : searchItem.trim();

        int pageSize = (normalizedSearch == null) ? 5 : 10;

        Pageable pageable = PageRequest.of(0, pageSize);

        List<FacultySearchResponse.SearchResultItem> result;

        if (normalizedSearch == null) {
            result = facultyRepository.initialFacultySearch(
                    classScheduleId,
                    pageable
            );
        } else {
            result = facultyRepository.findFacultySearch(
                    classScheduleId,
                    normalizedSearch,
                    pageable
            ).stream()
                    .map(f -> new FacultySearchResponse.SearchResultItem(
                            f.getId(),
                            f.getFirstName(),
                            f.getMiddleName(),
                            f.getLastName(),
                            f.getIsAssigned()
                    ))
                    .toList();
        }

        return new FacultySearchResponse(result);
    }
}
