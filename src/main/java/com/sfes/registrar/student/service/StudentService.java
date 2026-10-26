package com.sfes.registrar.student.service;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.course.Course;
import com.sfes.registrar.course.CourseRepository;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.student.SortBy;
import com.sfes.registrar.student.Student;
import com.sfes.registrar.student.StudentRepository;
import com.sfes.registrar.student.StudentStatus;
import com.sfes.registrar.student.dto.StudentRegistration;
import com.sfes.registrar.student.dto.StudentResponse;
import com.sfes.registrar.student.dto.StudentSemEnrollmentSearchResponse;
import com.sfes.registrar.student.dto.UpdateStudentRequest;
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
public class StudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void registerStudent(StudentRegistration registration) {
        String normalizedStudentId = NormalizationUtil.normalizeStudentId(registration.studentId());
        String normalizedEmail = NormalizationUtil.normalizeToLowerCase(registration.email());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(registration.courseCode());

        if (studentRepository.findByStudentId(normalizedStudentId).isPresent()) {
            throw new InvalidRequestException("Student ID already exists");
        }

        if (studentRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new InvalidRequestException("Email already exists");
        }

        Course course = courseRepository.findByCourseCode(normalizedCourseCode)
                .orElseThrow(() -> new InvalidRequestException("Course does not exist"));

        Student student = Student.builder()
                .studentId(normalizedStudentId)
                .email(normalizedEmail)
                .firstName(NormalizationUtil.normalizeName(registration.firstName()))
                .middleName(NormalizationUtil.normalizeName(registration.middleName()))
                .lastName(NormalizationUtil.normalizeName(registration.lastName()))
                .course(course)
                .status(StudentStatus.ACTIVE)
                .build();

        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudents(
            String departmentCode,
            String courseCode,
            int pageNumber,
            int size,
            SortBy sortBy,
            Sort.Direction direction,
            StudentStatus status
    ) {
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(departmentCode);
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(courseCode);

        Sort sort = Sort.by(direction, sortBy.getProperty()).and(Sort.by("id").ascending());

        Pageable pageable = PageRequest.of(pageNumber - 1, size, sort);

        Page<Student> result =
                studentRepository.findStudents(
                        normalizedDeptCode,
                        normalizedCourseCode,
                        status,
                        pageable
                );

        List<StudentResponse.StudentDTO> students = result.getContent()
                .stream()
                .map((student) -> new StudentResponse.StudentDTO(
                        student.getId(),
                        student.getStudentId(),
                        student.getEmail(),
                        student.getFirstName(),
                        student.getMiddleName(),
                        student.getLastName(),
                        student.getCourse().getCourseCode(),
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
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(request.courseCode());

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Student does not exist"));

        if (studentRepository.findByStudentIdAndIdNot(normalizedStudentId, id).isPresent()) {
            throw new InvalidRequestException("Student ID already exists");
        }

        if (studentRepository.findByEmailAndIdNot(normalizedEmail, id).isPresent()) {
            throw new InvalidRequestException("Email already exists");
        }
        Course course = courseRepository.findByCourseCode(normalizedCourseCode)
                .orElseThrow(() -> new InvalidRequestException("Course does not exist"));

        student.setStudentId(normalizedStudentId);
        student.setEmail(normalizedEmail);
        student.setFirstName(request.firstName());
        student.setMiddleName(request.middleName());
        student.setLastName(request.lastName());
        student.setCourse(course);
        student.setStatus(request.status());
    }

    @Transactional(readOnly = true)
    public StudentSemEnrollmentSearchResponse getStudentSemEnrollmentSearchResponse(
            Long semesterId,
            String studentId
    ) {
        String normalizedStudentId =
                NormalizationUtil.normalizeStudentId(studentId);

        return studentRepository.findStudentWithEnrollmentStatus(
                normalizedStudentId,
                semesterId
        ).orElse(null);
    }
}
