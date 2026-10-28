package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NameUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import com.sfes.registrar.records.classschedule.ClassScheduleRepository;
import com.sfes.registrar.records.classschedulesections.ClassScheduleSectionRepository;
import com.sfes.registrar.records.studentclassenrollment.dto.StudentClassEnrollmentProjection;
import com.sfes.registrar.records.studentclassenrollment.dto.StudentClassEnrollmentResponse;
import com.sfes.registrar.records.studentsemesterenrollment.StudentSemesterEnrollment;
import com.sfes.registrar.records.studentsemesterenrollment.StudentSemesterEnrollmentRepository;
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
public class StudentClassEnrollmentService {
    private final StudentClassEnrollmentRepository studentClassEnrollmentRepository;
    private final StudentRepository studentRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassScheduleSectionRepository classScheduleSectionRepository;
    private final StudentSemesterEnrollmentRepository studentSemesterEnrollmentRepository;

    @Transactional
    public void createStudentClassEnrollment(Long studentId, Long classScheduleId){
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new InvalidRequestException("Student does not exist"));

        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new InvalidRequestException("Class schedule does not exist"));

        boolean alreadyEnrolled =
                studentClassEnrollmentRepository
                        .existsByStudentIdAndClassScheduleIdAndStatus(
                                studentId,
                                classScheduleId,
                                Status.ACTIVE
                        );

        if (alreadyEnrolled) {
            throw new InvalidRequestException(
                    "Student is already enrolled in this class"
            );
        }

        StudentSemesterEnrollment studentSemesterEnrollment = studentSemesterEnrollmentRepository
                .findByStudentIdAndSemesterId(studentId, classSchedule.getClassOffering().getSemester().getId())
                .orElseThrow(() -> new InvalidRequestException("Student is not enrolled in this semester"));

        if (studentSemesterEnrollment.getSection() != null) {
            boolean sectionAlreadyEnrolled =
                    classScheduleSectionRepository
                            .findByClassScheduleIdAndSectionId(
                                    classScheduleId,
                                    studentSemesterEnrollment.getSection().getId()
                            )
                            .isPresent();

            if (sectionAlreadyEnrolled) {
                throw new InvalidRequestException(
                        "Student's section is already enrolled in this class"
                );
            } else {
                throw new InvalidRequestException(
                        "Student's section is not enrolled in this class. Please enroll the whole section instead."
                );
            }
        }

        StudentClassEnrollment studentClassEnrollment = StudentClassEnrollment.builder()
                .classSchedule(classSchedule)
                .student(student)
                .enrolledAt(Instant.now())
                .status(Status.ACTIVE)
                .build();

        studentClassEnrollmentRepository.save(studentClassEnrollment);
    }

    @Transactional(readOnly = true)
    public StudentClassEnrollmentResponse getStudentClassEnrollments(
            Long classScheduleId,
            int pageNumber,
            int pageSize
    ) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException(
                    "Page number and page size must be greater than zero"
            );
        }

        Long semesterId = classScheduleRepository
                .findSemesterIdByClassScheduleId(classScheduleId)
                .orElseThrow(() -> new InvalidRequestException("Class schedule not found"));

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<StudentClassEnrollmentProjection> result =
                studentClassEnrollmentRepository.findStudentClassEnrollments(
                        classScheduleId,
                        semesterId,
                        pageable
                );

        List<StudentClassEnrollmentResponse.StudentClassEnrollmentDto> enrollments =
                result.getContent()
                        .stream()
                        .map(s -> new StudentClassEnrollmentResponse.StudentClassEnrollmentDto(
                                s.getStudentId(),
                                s.getStudentIdNumber(),
                                NameUtil.fullName(
                                        s.getFirstName(),
                                        s.getLastName(),
                                        s.getMiddleName()
                                ),
                                s.getSectionName(),
                                s.getStatus()
                        ))
                        .toList();

        return new StudentClassEnrollmentResponse(
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
