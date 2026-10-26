package com.sfes.registrar.records.studentsemesterenrollment;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.Status;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemEnrollmentRequest;
import com.sfes.registrar.records.studentsemesterenrollment.dto.StudentSemesterEnrollmentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/student-semester-enrollment")
@RequiredArgsConstructor
public class StudentSemesterEnrollmentController {
    private final StudentSemesterEnrollmentService studentSemesterEnrollmentService;

    @PostMapping("/{semesterId}")
    public ApiMessage createStudentSemesterEnrollment(
            @PathVariable Long semesterId,
            @Valid @RequestBody StudentSemEnrollmentRequest request
    ) {
        studentSemesterEnrollmentService.createStudentSemesterEnrollment(semesterId, request);
        return new ApiMessage("Student semester enrollment created successfully.");
    }

    @GetMapping("/{semesterId}")
    public ApiResponse<StudentSemesterEnrollmentResponse> getStudentSemesterEnrollments(
            @PathVariable Long semesterId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam (required = false) String departmentCode,
            @RequestParam (required = false) String courseCode,
            @RequestParam (required = false) String sectionName,
            @RequestParam(required = false) Status status
    ) {
        return new ApiResponse<>(
                "Student semester enrollments retrieved successfully.",
                studentSemesterEnrollmentService.getStudentSemesterEnrollments(
                        semesterId,
                        page,
                        size,
                        departmentCode,
                        courseCode,
                        sectionName,
                        status
                )
        );
    }
}
