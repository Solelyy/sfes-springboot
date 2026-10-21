package com.sfes.registrar.yearlevel;

import com.sfes.registrar.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface YearLevelRepository extends JpaRepository<YearLevel, Long> {
    Optional<YearLevel> findByYearNumberAndCourse_CourseCode(int yearNumber, String courseCode);

    List<YearLevel> findByCourse_CourseCodeAndStatusOrderByYearNumberAsc(
            String courseCode,
            Status status
    );
}
