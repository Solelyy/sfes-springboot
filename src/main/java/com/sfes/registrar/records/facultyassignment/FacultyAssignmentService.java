package com.sfes.registrar.records.facultyassignment;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NameUtil;
import com.sfes.common.utility.TimeUtil;
import com.sfes.registrar.faculty.Faculty;
import com.sfes.registrar.faculty.FacultyRepository;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import com.sfes.registrar.records.classschedule.ClassScheduleRepository;
import com.sfes.registrar.records.facultyassignment.dto.FacultyAssignmentResponse;
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
public class FacultyAssignmentService {
    private final FacultyAssignmentRepository facultyAssignmentRepository;
    private final FacultyRepository facultyRepository;
    private final ClassScheduleRepository classScheduleRepository;

    @Transactional
    public void createFacultyAssignment(Long facultyId, Long classScheduleId){
        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new InvalidRequestException("Faculty member does not exist"));

        ClassSchedule classSchedule = classScheduleRepository.findById(classScheduleId)
                .orElseThrow(() -> new InvalidRequestException("Class schedule does not exist"));

        if (facultyAssignmentRepository
                .findByClassScheduleIdAndUnassignedAtIsNull(classScheduleId)
                .isPresent()) {
            throw new InvalidRequestException(
                    "A faculty member is already assigned to this class schedule"
            );
        }

        FacultyAssignment facultyAssignment = FacultyAssignment.builder()
                .faculty(faculty)
                .classSchedule(classSchedule)
                .assignedAt(Instant.now())
                .build();

        facultyAssignmentRepository.save(facultyAssignment);
    }

    @Transactional(readOnly = true)
    public FacultyAssignmentResponse getFacultyAssignments(Long semesterId, int pageNumber, int pageSize) {

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<FacultyAssignment> result = facultyAssignmentRepository.findFacultyAssignments(semesterId, pageable);

        List<FacultyAssignmentResponse.FacultyAssignmentDto> facultyAssignments = result.getContent()
                .stream()
                .map((f) -> new FacultyAssignmentResponse.FacultyAssignmentDto(
                        f.getId(),
                        NameUtil.fullName(
                                f.getFaculty().getFirstName(),
                                f.getFaculty().getLastName(),
                                f.getFaculty().getMiddleName()
                        ),
                        f.getClassSchedule().getClassOffering().getSubject().getSubjectCode(),
                        f.getClassSchedule().getDayOfWeek(),
                        TimeUtil.formatTime(f.getClassSchedule().getStartTime()),
                        TimeUtil.formatTime(f.getClassSchedule().getEndTime())
                ))
                .toList();

        return new FacultyAssignmentResponse(
                facultyAssignments,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }
}
