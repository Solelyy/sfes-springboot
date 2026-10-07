package com.sfes.registrar.yearlevel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YearLevelRepository extends JpaRepository<YearLevel, Long> {
    Optional<YearLevel> findByYearNumberAndCourse_CourseCode(int yearNumber, String courseCode);
}
