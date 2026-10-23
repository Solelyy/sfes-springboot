package com.sfes.registrar.records.classschedule;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.common.utility.TimeUtil;
import com.sfes.registrar.faculty.dto.FacultySearchResponse;
import com.sfes.registrar.records.classoffering.ClassOffering;
import com.sfes.registrar.records.classoffering.ClassOfferingRepository;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleRequest;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleResponse;
import com.sfes.registrar.records.classschedulesections.ClassScheduleSection;
import com.sfes.registrar.records.classschedulesections.ClassScheduleSectionRepository;
import com.sfes.registrar.records.facultyassignment.FacultyAssignment;
import com.sfes.registrar.records.facultyassignment.FacultyAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClassScheduleService {
    private final ClassOfferingRepository classOfferingRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final ClassScheduleSectionRepository classScheduleSectionRepository;
    private final FacultyAssignmentRepository facultyAssignmentRepository;

    @Transactional
    public void createClassSchedule(Long classOfferingId, ClassScheduleRequest request){
        String normalizedRoom = NormalizationUtil.normalizeToUpperCase(request.room());
        LocalTime normalizeStartTime = TimeUtil.normalizeTime(request.startTime());
        LocalTime normalizeEndTime = TimeUtil.normalizeTime(request.endTime());

        TimeUtil.validateStartEndTime(normalizeStartTime, normalizeEndTime);

        ClassOffering classOffering = classOfferingRepository.findById(classOfferingId)
                .orElseThrow(() -> new InvalidRequestException("Class offering does not exist"));

        if (classScheduleRepository.findByDayOfWeekAndStartTimeAndEndTimeAndRoomAndClassOfferingId (
                request.dayOfWeek(),
                normalizeStartTime,
                normalizeEndTime,
                normalizedRoom,
                classOfferingId
        ).isPresent()) {
            throw new InvalidRequestException("Class schedule already exists");
        }

        ClassSchedule classSchedule = ClassSchedule.builder()
                .classOffering(classOffering)
                .dayOfWeek(request.dayOfWeek())
                .room(normalizedRoom)
                .startTime(normalizeStartTime)
                .endTime(normalizeEndTime)
                .build();

        classScheduleRepository.save(classSchedule);
    }

    @Transactional(readOnly = true)
    public ClassScheduleResponse getClassSchedules(
            Long classOfferingId,
            int pageNumber,
            int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<ClassSchedule> result = classScheduleRepository
                .findClassSchedules(classOfferingId, pageable);

        List<ClassSchedule> schedules = result.getContent();

        Meta meta = new Meta(
                result.getNumber() + 1,
                result.getSize(),
                result.getTotalPages(),
                result.getTotalElements()
        );

        if (schedules.isEmpty()) {
            return new ClassScheduleResponse(
                    List.of(),
                    meta
            );
        }

        List<Long> scheduleIds = schedules.stream()
                .map(ClassSchedule::getId)
                .toList();

        List<ClassScheduleSection> assignments =
                classScheduleSectionRepository.findAllByClassScheduleIdsWithSection(scheduleIds);

        Map<Long, List<ClassScheduleResponse.ClassScheduleDto.SectionSummary>>
                sectionsByScheduleId = assignments.stream()
                .collect(Collectors.groupingBy(
                        assignment ->
                                assignment.getClassSchedule().getId(),
                        Collectors.mapping(
                                assignment ->
                                        new ClassScheduleResponse.ClassScheduleDto.SectionSummary(
                                                assignment.getSection().getId(),
                                                assignment.getSection().getSectionName()
                                        ),
                                Collectors.toList()
                        )
                ));

        List<FacultyAssignment> facultyAssignments = facultyAssignmentRepository
                .findActiveAssignmentsByClassScheduleIds(scheduleIds);

        Map<Long, FacultySearchResponse.AssignedFaculty> facultyByScheduleId =
                facultyAssignments.stream()
                        .collect(Collectors.toMap(
                                assignment ->
                                        assignment.getClassSchedule().getId(),
                                assignment -> {
                                    var faculty = assignment.getFaculty();

                                    return new FacultySearchResponse.AssignedFaculty(
                                            faculty.getId(),
                                            faculty.getFirstName(),
                                            faculty.getMiddleName(),
                                            faculty.getLastName()
                                    );
                                }
                        ));

        List<ClassScheduleResponse.ClassScheduleDto> classSchedules = schedules
                .stream()
                .map(schedule -> {
                    var subject = schedule
                            .getClassOffering()
                            .getSubject();

                    return new ClassScheduleResponse.ClassScheduleDto(
                            schedule.getId(),
                            schedule.getDayOfWeek(),
                            TimeUtil.formatTime(schedule.getStartTime()),
                            TimeUtil.formatTime(schedule.getEndTime()),
                            schedule.getRoom(),
                            subject.getSubjectName(),
                            subject.getSubjectCode(),
                            sectionsByScheduleId.getOrDefault(
                                    schedule.getId(),
                                    List.of()
                            ),
                            facultyByScheduleId.get(schedule.getId())
                    );
                })
                .toList();

        return new ClassScheduleResponse(classSchedules, meta);
    }
}
