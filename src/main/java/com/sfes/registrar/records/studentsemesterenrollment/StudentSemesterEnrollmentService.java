package com.sfes.registrar.records.studentsemesterenrollment;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NameUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.semester.Semester;
import com.sfes.registrar.records.semester.SemesterRepository;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemEnrollmentProjection;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemEnrollmentRequest;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemesterEnrollmentResponse;
import com.sfes.registrar.section.Section;
import com.sfes.registrar.section.SectionRepository;
import com.sfes.registrar.student.Student;
import com.sfes.registrar.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentSemesterEnrollmentService {
    private final StudentSemesterEnrollmentRepository studentSemesterEnrollmentRepository;
    private final SemesterRepository semesterRepository;
    private final StudentRepository studentRepository;
    private final SectionRepository sectionRepository;


    @Transactional
    public void createStudentSemesterEnrollment(Long semesterId, StudentSemEnrollmentRequest request) {
        Semester semester =semesterRepository.findById(semesterId)
                .orElseThrow(() -> new InvalidRequestException("Semester not found"));

        Student student = studentRepository.findById(request.studentId())
                .orElseThrow(() -> new InvalidRequestException("Student not found"));

        Section section = sectionRepository.findById(request.sectionId())
                .orElseThrow(() -> new InvalidRequestException("Section not found"));

        if(studentSemesterEnrollmentRepository.findByStudentIdAndSemesterId(
                request.studentId(),
                semesterId
        ).isPresent()) {
            throw new InvalidRequestException("Student is already enrolled in this semester");
        }

        //validate if section is in the same course of the student

        StudentSemesterEnrollment studentSemesterEnrollment = StudentSemesterEnrollment.builder()
                .semester(semester)
                .student(student)
                .section(section)
                .yearLevel(request.yearLevel())
                .enrolledAt(Instant.now())
                .status(Status.ACTIVE)
                .build();
        studentSemesterEnrollmentRepository.save(studentSemesterEnrollment);
    }

    @Transactional(readOnly = true)
    public StudentSemesterEnrollmentResponse getStudentSemesterEnrollments(
            Long semesterId,
            int pageNumber,
            int pageSize,
            String departmentCode,
            String courseCode,
            String sectionName,
            Status status
    ) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<StudentSemEnrollmentProjection> result =
                studentSemesterEnrollmentRepository.findSemesterEnrollment(
                semesterId,
                departmentCode,
                courseCode,
                sectionName,
                status,
                pageable
        );

        List<StudentSemesterEnrollmentResponse.StudentSemesterEnrollmentDto> enrollments =
                result.getContent().stream()
                .map(enrollment -> new StudentSemesterEnrollmentResponse.StudentSemesterEnrollmentDto(
                        enrollment.getId(),
                        enrollment.getStudentId(),
                        NameUtil.fullName(enrollment.getFirstName(), enrollment.getMiddleName(), enrollment.getLastName()),
                        enrollment.getCourseCode(),
                        enrollment.getYearLevel(),
                        enrollment.getSectionName(),
                        enrollment.getStatus()
                ))
                .toList();

        return new StudentSemesterEnrollmentResponse(
                enrollments,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }
}
