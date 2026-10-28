package com.sfes.registrar.records.studentclassenrollment;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.records.studentclassenrollment.dto.StudentClassEnrollmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/studentclassenrollments")
@RequiredArgsConstructor
public class StudentClassEnrollmentController {
    private final StudentClassEnrollmentService studentClassEnrollmentService;

    @PostMapping("/{classScheduleId}/{studentId}")
    public ApiMessage createStudentClassEnrollment(
            @PathVariable Long studentId,
            @PathVariable Long classScheduleId
    ){
        studentClassEnrollmentService.createStudentClassEnrollment(studentId, classScheduleId);

        return new ApiMessage("Student successfully enrolled in a class");
    }

    @GetMapping("/{classScheduleId}")
    public ApiResponse<StudentClassEnrollmentResponse> getStudentClassEnrollments(
            @PathVariable Long classScheduleId,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return new ApiResponse<>(
                "Student class enrollments successfully retrieved",
                studentClassEnrollmentService.getStudentClassEnrollments(classScheduleId, pageNumber, pageSize)
        );
    }
}
