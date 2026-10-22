
package com.sfes.registrar.records.classschedulesections;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.registrar.records.classschedule.ClassSchedule;
import com.sfes.registrar.records.classschedule.ClassScheduleRepository;
import com.sfes.registrar.section.Section;
import com.sfes.registrar.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClassScheduleSectionService {

    private final ClassScheduleSectionRepository
            classScheduleSectionRepository;

    private final ClassScheduleRepository classScheduleRepository;
    private final SectionRepository sectionRepository;

    @Transactional
    public void createClassScheduleSection(
            Long classScheduleId,
            Long sectionId
    ) {
        ClassSchedule classSchedule =
                classScheduleRepository.findById(classScheduleId)
                        .orElseThrow(() -> new InvalidRequestException("Class schedule does not exist"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new InvalidRequestException("Section does not exist"));

        boolean alreadyAssigned =
                classScheduleSectionRepository
                        .findByClassScheduleIdAndSectionId(classScheduleId, sectionId)
                        .isPresent();

        if (alreadyAssigned) {
            throw new InvalidRequestException(
                    "The section is already assigned to the class schedule"
            );
        }

        ClassScheduleSection classScheduleSection = new ClassScheduleSection();
        classScheduleSection.setClassSchedule(classSchedule);
        classScheduleSection.setSection(section);

        classScheduleSectionRepository.save(classScheduleSection);
    }
}
