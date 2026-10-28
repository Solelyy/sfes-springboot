package com.sfes.registrar.records.classschedulesections;

import com.sfes.common.classes.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registrar/class-schedule-sections")
@RequiredArgsConstructor
public class ClassScheduleSectionController {
    private final ClassScheduleSectionService classScheduleSectionService;

    @PostMapping("/{classScheduleId}/{sectionId}")
    public ApiMessage createClassScheduleSection(
            @PathVariable Long classScheduleId,
            @PathVariable Long sectionId
    ){
        classScheduleSectionService.createClassScheduleSection(classScheduleId, sectionId);

        return new ApiMessage("Class schedule section successfully added");
    }
}
