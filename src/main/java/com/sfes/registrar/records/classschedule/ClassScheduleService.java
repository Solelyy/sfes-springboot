package com.sfes.registrar.records.classschedule;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.common.utility.TimeUtil;
import com.sfes.registrar.records.classoffering.ClassOffering;
import com.sfes.registrar.records.classoffering.ClassOfferingRepository;
import com.sfes.registrar.records.classoffering.dto.ClassOfferingResponse;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleRequest;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassScheduleService {
    private final ClassOfferingRepository classOfferingRepository;
    private final ClassScheduleRepository classScheduleRepository;

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

        TimeUtil.validateStartEndTime(normalizeStartTime, normalizeEndTime);

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
            Long semesterId,
            int pageNumber,
            int pageSize
    ){
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        Page<ClassSchedule> result = classScheduleRepository
                .findClassSchedules(semesterId, pageable);

        List<ClassScheduleResponse.ClassScheduleDto> classSchedules = result.getContent()
                .stream()
                .map((c) -> new ClassScheduleResponse.ClassScheduleDto(
                        c.getId(),
                        c.getDayOfWeek(),
                        TimeUtil.formatTime(c.getStartTime()),
                        TimeUtil.formatTime(c.getEndTime()),
                        c.getRoom(),
                        c.getClassOffering().getSubject().getSubjectName(),
                        c.getClassOffering().getSubject().getSubjectCode()
                ))
                .toList();

        return new ClassScheduleResponse(
                classSchedules,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }
}
