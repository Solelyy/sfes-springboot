package com.sfes.registrar.subjects;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.student.Student;
import com.sfes.registrar.subjects.dto.SubjectRequest;
import com.sfes.registrar.subjects.dto.SubjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void addSubject(SubjectRequest request){
        String normalizedSubjectName = NormalizationUtil.normalizeToUpperCase(request.subjectName());
        String normalizedSubjectCode = NormalizationUtil.normalizeToUpperCase(request.subjectCode());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        if (subjectRepository.findBySubjectName(normalizedSubjectName)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject name already exists");
        }

        if (subjectRepository.findBySubjectCode(normalizedSubjectCode)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject code already exists");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        Subject subject = Subject.builder()
                .subjectName(normalizedSubjectName)
                .subjectCode(normalizedSubjectCode)
                .department(department)
                .unitCount(request.unitCount())
                .build();

        subjectRepository.save(subject);
    }

    public SubjectResponse getSubjects(String departmentCode, int pageNumber, int size) {
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(departmentCode);

        Sort sort = Sort.by("subjectCode").ascending();

        Pageable pageable = PageRequest.of(pageNumber -1,size, sort);

        Page<Subject> result = subjectRepository.findSubjects(normalizedDeptCode, pageable);

        List<SubjectResponse.SubjectDto> subjects = result.stream()
                .map((subject) -> new SubjectResponse.SubjectDto(
                        subject.getId(),
                        subject.getSubjectName(),
                        subject.getSubjectCode(),
                        subject.getUnitCount(),
                        subject.getDepartment().getDepartmentCode()
                ))
                .toList();

        return new SubjectResponse(
                subjects,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    @Transactional
    public void updateSubject(Long id, SubjectRequest request) {
        String normalizedSubjectName = NormalizationUtil.normalizeToUpperCase(request.subjectName());
        String normalizedSubjectCode = NormalizationUtil.normalizeToUpperCase(request.subjectCode());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Student does not exist"));

        if (subjectRepository
                .findBySubjectNameAndIdNot(normalizedSubjectName, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject name already exist");
        }

        if (subjectRepository
                .findBySubjectCodeAndIdNot(normalizedSubjectCode, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject code already exist");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        subject.setSubjectCode(normalizedSubjectCode);
        subject.setSubjectName(normalizedSubjectName);
        subject.setUnitCount(request.unitCount());
        subject.setDepartment(department);
    }
}