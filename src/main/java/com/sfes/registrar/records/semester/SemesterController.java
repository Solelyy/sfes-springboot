package com.sfes.registrar.records.semester;


import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.semester.dto.SemesterRequest;
import com.sfes.registrar.records.semester.dto.SemesterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registrar/semesters")
@RequiredArgsConstructor
public class SemesterController {
    private final SemesterService semesterService;

    @PostMapping("/{schoolYearId}")
    public ApiMessage createSemester(@PathVariable Long schoolYearId, @Valid @RequestBody SemesterRequest request) {
        semesterService.createSemester(schoolYearId, request);

        return new ApiMessage("Semester added successfully.");
    }

    @GetMapping("/{schoolYearId}")
    public ApiResponse<SemesterResponse> getSemesters(@PathVariable Long schoolYearId){

        return new ApiResponse<>(
                "Successfully retrieved semesters",
                semesterService.getSemesters(schoolYearId)
        );
    }
}
