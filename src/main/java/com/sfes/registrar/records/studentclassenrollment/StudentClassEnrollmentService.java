package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NameUtil;
import com.sfes.common.utility.TimeUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import com.sfes.registrar.records.classschedule.ClassScheduleRepository;
import com.sfes.registrar.records.studentclassenrollment.dto.StudentClassEnrollmentResponse;
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

        StudentClassEnrollment studentClassEnrollment = StudentClassEnrollment.builder()
                .classSchedule(classSchedule)
                .student(student)
                .enrolledAt(Instant.now())
                .status(Status.ACTIVE)
                .build();

        studentClassEnrollmentRepository.save(studentClassEnrollment);
    }

    @Transactional(readOnly = true)
    public StudentClassEnrollmentResponse getStudentClassEnrollments(Long semesterId, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<StudentClassEnrollment> result = studentClassEnrollmentRepository.findStudentClassEnrollments(semesterId, pageable);

        List<StudentClassEnrollmentResponse.StudentClassEnrollmentDto> studentClassEnrollments = result.getContent()
                .stream()
                .map((s ) -> new StudentClassEnrollmentResponse.StudentClassEnrollmentDto(
                        s.getId(),
                        s.getStudent().getStudentId(),
                        NameUtil.fullName(
                                s.getStudent().getFirstName(),
                                s.getStudent().getLastName(),
                                s.getStudent().getMiddleName()
                        ),
                        s.getClassSchedule().getClassOffering().getSubject().getSubjectCode(),
                        s.getClassSchedule().getDayOfWeek(),
                        TimeUtil.formatTime(s.getClassSchedule().getStartTime()),
                        TimeUtil.formatTime(s.getClassSchedule().getEndTime())
                ))
                .toList();

        return new StudentClassEnrollmentResponse(
                studentClassEnrollments,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }
}
