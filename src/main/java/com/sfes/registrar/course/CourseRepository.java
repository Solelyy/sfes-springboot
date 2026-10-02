package com.sfes.registrar.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
     Optional<Course> findByCourseName(String courseName);
     Optional<Course> findByCourseCode(String courseCode);

     @Query("""
        SELECT c
        FROM Course c
        JOIN FETCH c.department d
        WHERE (:departmentCode IS NULL OR d.departmentCode = :departmentCode)
    """)
     List<Course> findCourses(@Param("departmentCode") String departmentCode);

     Optional<Course> findByCourseCodeAndIdNot(String courseCode, Long id);

     Optional<Course> findByCourseNameAndIdNot(String courseName, Long id);
}
