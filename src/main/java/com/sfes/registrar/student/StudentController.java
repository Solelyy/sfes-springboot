package com.sfes.registrar.student;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.faculty.SortBy;
import com.sfes.registrar.student.dto.StudentRegistration;
import com.sfes.registrar.student.dto.StudentResponse;
import com.sfes.registrar.student.dto.UpdateStudentRequest;
import com.sfes.registrar.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registrar/students")
public class StudentController {
    private final StudentService studentService;

    @PostMapping
    public ApiMessage registerStudent(@Valid @RequestBody StudentRegistration request) {
        studentService.registerStudent(request);

        return new ApiMessage("Student added successfully");
    }

    @GetMapping
    public ApiResponse<StudentResponse> getStudents(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "STUDENT_ID") SortBy sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection,
            @RequestParam(required = false) String departmentCode,
            @RequestParam(required = false) StudentStatus status
    ){
        return new ApiResponse<>(
                "Students retrieved successfully",
                studentService.getStudents(departmentCode, pageNumber, size, sortBy, sortDirection, status)
        );
    }

    @PatchMapping("{id}")
    public ApiMessage updateStudent(@PathVariable Long id, @Valid @RequestBody UpdateStudentRequest request) {
        studentService.updateStudent(id, request);

        return new ApiMessage("Student record updated successfully");
    }
}
