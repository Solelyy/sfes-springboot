package com.sfes.registrar.records.schoolyear;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {
    Optional<SchoolYear> findBySchoolYear(String schoolYear);

    Optional<SchoolYear> findBySchoolYearAndIdNot(String schoolYear, Long id);
}
