package com.sfes.registrar.records.classschedule;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleRequest;
import com.sfes.registrar.records.classschedule.dto.ClassScheduleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/classschedules")
@RequiredArgsConstructor
public class ClassScheduleController {
    private final ClassScheduleService classScheduleService;

    @PostMapping("/{classOfferingId}")
    public ApiMessage createClassSchedule(@PathVariable Long classOfferingId, @Valid @RequestBody ClassScheduleRequest request){
        classScheduleService.createClassSchedule(classOfferingId, request);

        return new ApiMessage("Class schedule successfully created");
    }

    @GetMapping("/{classOfferingId}")
    public ApiResponse<ClassScheduleResponse> getClassSchedules(
            @PathVariable Long classOfferingId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
            ){
        return new ApiResponse<>(
                "Class schedules successfully retrieved",
                classScheduleService.getClassSchedules(classOfferingId, pageNumber, pageSize)
        );
    }
}
