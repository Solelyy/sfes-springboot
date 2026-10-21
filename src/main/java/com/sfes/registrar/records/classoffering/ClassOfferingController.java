package com.sfes.registrar.records.classoffering;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.classoffering.dto.ClassOfferingRequest;
import com.sfes.registrar.records.classoffering.dto.ClassOfferingResponse;
import com.sfes.registrar.records.classoffering.dto.SubjectSearchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/classofferings")
@RequiredArgsConstructor
public class ClassOfferingController {
    private final ClassOfferingService classOfferingService;

    @PostMapping("/{semesterId}")
    public ApiMessage createClassOffering(
            @PathVariable Long semesterId,
            @Valid @RequestBody ClassOfferingRequest request){
        classOfferingService.createClassOffering(semesterId, request);

        return new ApiMessage("Class offering successfully created");
    }

    @GetMapping("/{semesterId}")
    public ApiResponse<ClassOfferingResponse> getClassOfferings(
            @PathVariable Long semesterId,
            @RequestParam(required = false) String courseCode,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){

        return new ApiResponse<>(
                "Class offerings successfully retrieved",
                classOfferingService.getClassOfferings(courseCode, semesterId, pageNumber, pageSize)
        );
    }

    @GetMapping("/available/{semesterId}")
    public ApiResponse<SubjectSearchResult> getSearchResponse(
            @PathVariable Long semesterId,
            @RequestParam(required = false) String searchItem
    ){
        return new ApiResponse<>(
                "Search results retrieved successfully",
                classOfferingService.getSearchResponses(semesterId, searchItem)
        );
    }
}