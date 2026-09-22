package com.sfes.registrar.student.service;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.faculty.SortBy;
import com.sfes.registrar.student.Student;
import com.sfes.registrar.student.StudentRepository;
import com.sfes.registrar.student.StudentStatus;
import com.sfes.registrar.student.dto.StudentRegistration;
import com.sfes.registrar.student.dto.StudentResponse;
import com.sfes.registrar.student.dto.UpdateStudentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void registerStudent(StudentRegistration registration) {
        String normalizedStudentId = NormalizationUtil.normalizeStudentId(registration.studentId());
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(registration.email());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(registration.departmentCode());

        if (studentRepository.findByStudentId(normalizedStudentId).isPresent()) {
            throw new InvalidRequestException("Student ID already exists");
        }

        if (studentRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new InvalidRequestException("Email already exists");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        Student student = Student.builder()
                .studentId(normalizedStudentId)
                .email(normalizedEmail)
                .firstName(NormalizationUtil.normalizeName(registration.firstName()))
                .middleName(NormalizationUtil.normalizeName(registration.middleName()))
                .lastName(NormalizationUtil.normalizeName(registration.lastName()))
                .department(department)
                .status(StudentStatus.ACTIVE)
                .build();

        studentRepository.save(student);
    }

    public StudentResponse getStudents(String departmentCode, int pageNumber, int size, SortBy sortBy, Sort.Direction direction, StudentStatus status) {
        Sort sort = Sort.by(direction, sortBy.getProperty()).and(Sort.by("id").ascending());

        Pageable pageable = PageRequest.of(pageNumber - 1, size, sort);

        Page<Student> result = studentRepository.findStudents(departmentCode, status, pageable);

        List<StudentResponse.StudentDTO> students = result.getContent()
                .stream()
                .map((student) -> new StudentResponse.StudentDTO(
                        student.getId(),
                        student.getStudentId(),
                        student.getEmail(),
                        student.getFirstName(),
                        student.getMiddleName(),
                        student.getLastName(),
                        student.getDepartment().getDepartmentCode(),
                        student.getStatus()
                ))
                .toList();

        return new StudentResponse(
                students,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    @Transactional
    public void updateStudent(Long id, UpdateStudentRequest request) {
        String normalizedStudentId = NormalizationUtil.normalizeStudentId(request.studentId());
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(request.email());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Student does not exist"));

        if (studentRepository.findByStudentIdAndIdNot(normalizedStudentId, id).isPresent()) {
            throw new InvalidRequestException("Student ID already exists");
        }

        if (studentRepository.findByEmailAndIdNot(normalizedEmail, id).isPresent()) {
            throw new InvalidRequestException("Email already exists");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        student.setStudentId(normalizedStudentId);
        student.setEmail(normalizedEmail);
        student.setFirstName(request.firstName());
        student.setMiddleName(request.middleName());
        student.setLastName(request.lastName());
        student.setDepartment(department);
        student.setStatus(request.status());
    }
}
