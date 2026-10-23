package com.sfes.registrar.subject;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.subject.dto.SubjectRequest;
import com.sfes.registrar.subject.dto.SubjectResponse;
import com.sfes.registrar.subject.dto.SubjectSearchResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ApiMessage addSubject(@Valid @RequestBody SubjectRequest request) {
        subjectService.addSubject(request);
        return new ApiMessage("Subject added successfully");
    }

    @GetMapping
    public ApiResponse<SubjectResponse> getSubjects(
            @RequestParam(required = false) String courseCode,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int size

    ) {
        return new ApiResponse<>(
                "Subjects retrieved successfully",
                subjectService.getSubjects(courseCode, pageNumber, size)
        );
    }

    @PatchMapping("/{id}")
    public ApiMessage updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        subjectService.updateSubject(id, request);

        return new ApiMessage("Subject updated successfully");
    }

    @GetMapping("/{semesterId}")
    public ApiResponse<SubjectSearchResult> getSearchResponse(
            @PathVariable Long semesterId,
            @RequestParam(required = false) String searchItem
    ){
        return new ApiResponse<>(
                "Search results retrieved successfully",
                subjectService.getSearchResponses(semesterId, searchItem)
        );
    }
}
