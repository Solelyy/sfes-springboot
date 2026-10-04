package com.sfes.registrar.course;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.course.dto.Courses;
import com.sfes.registrar.course.dto.CreateCourse;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public void createCourse(CreateCourse request){
        String normalizedCourseName = NormalizationUtil.normalizeToUpperCase(request.courseName());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(request.courseCode());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        if (courseRepository.findByCourseName(normalizedCourseName).isPresent()) {
            throw new InvalidRequestException("Course name already exists");
        }

        if (courseRepository.findByCourseCode(normalizedCourseCode).isPresent()) {
            throw new InvalidRequestException("Course code already exists");
        }

        Department department = departmentRepository.findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        Course course = Course.builder()
                .courseName(normalizedCourseName)
                .courseCode(normalizedCourseCode)
                .department(department)
                .build();

        courseRepository.save(course);
    }

    public Courses getCourses(String departmentCode) {
        String normalizedDeptCode =
                NormalizationUtil.normalizeToUpperCase(departmentCode);

        List<Courses.Course> courses = courseRepository.findCourses(normalizedDeptCode)
                .stream()
                .map((course) -> new Courses.Course(
                        course.getId(),
                        course.getCourseName(),
                        course.getCourseCode(),
                        course.getDepartment().getDepartmentCode()
                ))
                .toList();

        return new Courses(
                courses
        );
    }

    @Transactional
    public void updateCourse(Long id, CreateCourse request){
        //validate if the course name and course code is not existing
        //validate if the department code exists

        String normalizedCourseName = NormalizationUtil.normalizeToUpperCase(request.courseName());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(request.courseCode());
        String normalizedDeptCode = NormalizationUtil.normalizeToUpperCase(request.departmentCode());

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Course does not exist"));

        if (courseRepository
                .findByCourseNameAndIdNot(normalizedCourseName, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Course name already exists.");
        }

        if (courseRepository
                .findByCourseCodeAndIdNot(normalizedCourseCode, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Course code already exists.");
        }

        Department department = departmentRepository
                .findByDepartmentCode(normalizedDeptCode)
                .orElseThrow(() -> new InvalidRequestException("Department does not exist"));

        course.setCourseName(normalizedCourseName);
        course.setCourseCode(normalizedCourseCode);
        course.setDepartment(department);
    }
}
