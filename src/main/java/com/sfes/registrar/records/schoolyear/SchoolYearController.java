package com.sfes.registrar.records.schoolyear;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.schoolyear.dto.SchoolYearRequest;
import com.sfes.registrar.records.schoolyear.dto.SchoolYearResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/schoolyears")
@RequiredArgsConstructor
public class SchoolYearController {
    private final SchoolYearService schoolYearService;

    @PostMapping
    public ApiMessage createSchoolYear(@Valid @RequestBody SchoolYearRequest request){
        schoolYearService.createSchoolYear(request);

        return  new ApiMessage("School year successfully created.");
    }

    @GetMapping
    public ApiResponse<SchoolYearResponse> getSchoolYears(){
        return new ApiResponse<>(
                "School years retrieved successfully",
                schoolYearService.getSchoolYears()
        );
    }

    @PatchMapping("/{id}")
    public ApiMessage updateSchoolYear(@PathVariable Long id, @Valid @RequestBody SchoolYearRequest request){
        schoolYearService.updateSchoolYear(id, request);

        return new ApiMessage("School year successfully updated");
    }
}
