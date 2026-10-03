package com.sfes.registrar.records.semester;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findBySchoolYearIdAndSemesterType(Long schoolYearId, SemesterType semesterType);

    List<Semester> findAllBySchoolYearIdOrderBySemesterTypeAsc(Long schoolYearId);
}