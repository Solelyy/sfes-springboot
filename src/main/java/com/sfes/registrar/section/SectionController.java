package com.sfes.registrar.section;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.section.dto.SectionRequest;
import com.sfes.registrar.section.dto.SectionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/sections")
@RequiredArgsConstructor
public class SectionController {
    private final SectionService sectionService;

    @PostMapping("/{courseCode}/{yearNumber}")
    public ApiMessage createSection(
            @PathVariable String courseCode,
            @PathVariable int yearNumber,
            @Valid @RequestBody SectionRequest request
    ){
        sectionService.createSection(courseCode, yearNumber,request);

        return new ApiMessage("Section added successfully");
    }

    @GetMapping("/{courseCode}/{yearNumber}")
    public ApiResponse<SectionResponse> getSections(
            @PathVariable String courseCode,
            @PathVariable int yearNumber,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){

        return new ApiResponse<>(
                "Successfully retrieved sections",
                sectionService.getSections(courseCode, yearNumber, pageNumber, pageSize)
        );
    }

    @PatchMapping("/{id}")
    public ApiMessage updateSection(@PathVariable Long id, @Valid @RequestBody SectionRequest request) {
        sectionService.updateSection(id, request);
        return new ApiMessage("Section updated successfully");
    }
}
