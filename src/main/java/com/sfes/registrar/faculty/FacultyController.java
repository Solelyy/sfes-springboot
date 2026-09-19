package com.sfes.registrar.faculty;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.faculty.dto.FacultyResponse;
import com.sfes.registrar.faculty.dto.ImportFacultyResponse;
import com.sfes.registrar.faculty.dto.RegisterFacultyRequest;
import com.sfes.registrar.faculty.dto.UpdateFacultyRequest;
import com.sfes.registrar.faculty.service.FacultyService;
import com.sfes.registrar.faculty.service.ImportFacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/registrar/faculty")
@RequiredArgsConstructor
public class FacultyController {
    private final FacultyService facultyService;
    private final ImportFacultyService importFacultyService;

    @PostMapping
    public ApiMessage addFacultyMember(@Valid @RequestBody RegisterFacultyRequest request){
        facultyService.addFacultyMember(request);

        return new ApiMessage("Successfully added faculty member");
    }

    @PatchMapping("{id}")
    public ApiMessage updateFaculty(@PathVariable Long id, @Valid @RequestBody UpdateFacultyRequest request) {
        facultyService.updateFaculty(id, request);

        return new ApiMessage("Faculty member updated successfully");
    }

    @GetMapping
    public ApiResponse<FacultyResponse> getFaculty(@RequestParam(required = false) String departmentCode){
        return new ApiResponse<>(
                "Retrieved faculty members successfully",
                facultyService.getFaculty(departmentCode)
        );
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public ApiResponse<ImportFacultyResponse> uploadFaculty(
            @RequestParam("file") MultipartFile file
    ) {

        return new ApiResponse<>(
                "Faculty import completed",
                importFacultyService.importFaculty(file)
                );
    }
}
