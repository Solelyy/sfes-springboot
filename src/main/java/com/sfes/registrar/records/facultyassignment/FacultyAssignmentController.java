package com.sfes.registrar.records.facultyassignment;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.facultyassignment.dto.FacultyAssignmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/faculty-assignments")
@RequiredArgsConstructor
public class FacultyAssignmentController {
    private final FacultyAssignmentService facultyAssignmentService;

    @PostMapping("/{facultyId}/{classScheduleId}")
    public ApiMessage createFacultyAssignment(
            @PathVariable Long facultyId,
            @PathVariable Long classScheduleId
    ){
        facultyAssignmentService.createFacultyAssignment(facultyId, classScheduleId);

        return new ApiMessage("Successfully created faculty assignment");
    }

    @GetMapping("/{semesterId}")
    public ApiResponse<FacultyAssignmentResponse> getFacultyAssignments(
            @PathVariable Long semesterId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
            ) {
        return new ApiResponse<>(
                "Faculty assignments retrieved successfully",
                facultyAssignmentService.getFacultyAssignments(semesterId, pageNumber, pageSize)
        );
    }
}
