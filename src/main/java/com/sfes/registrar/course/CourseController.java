package com.sfes.registrar.course;

import com.sfes.common.classes.ApiMessage;
import com.sfes.common.classes.ApiResponse;
import com.sfes.registrar.course.dto.Courses;
import com.sfes.registrar.course.dto.CreateCourse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registrar/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping
    public ApiMessage createCourse(@Valid @RequestBody CreateCourse request) {
        courseService.createCourse(request);

        return new ApiMessage("Course added successfully.");
    }

    @GetMapping
    public ApiResponse<Courses> getCourses(@RequestParam(required = false) String departmentCode){
        return new ApiResponse<>(
                "Courses retrieved successfully.",
                courseService.getCourses(departmentCode)
        );
    }

    @PatchMapping("{id}")
    public ApiMessage updateCourse(@PathVariable Long id, @Valid @RequestBody CreateCourse request){
        courseService.updateCourse(id, request);

        return new ApiMessage("Course updated successfully.");
    }
}
