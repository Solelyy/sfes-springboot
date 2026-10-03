package com.sfes.registrar.records.semester;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.registrar.records.schoolyear.SchoolYear;
import com.sfes.registrar.records.schoolyear.SchoolYearRepository;
import com.sfes.registrar.records.semester.dto.SemesterRequest;
import com.sfes.registrar.records.semester.dto.SemesterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterService {
    private final SemesterRepository semesterRepository;
    private final SchoolYearRepository schoolYearRepository;

    @Transactional
    public void createSemester(Long schoolYearId, SemesterRequest request){
        SchoolYear schoolYear = schoolYearRepository.findById(schoolYearId)
                .orElseThrow(() -> new InvalidRequestException("School year does not exist"));

        if (semesterRepository
                .findBySchoolYearIdAndSemesterType(schoolYearId, request.semesterType())
                .isPresent()){
            throw new InvalidRequestException("Semester already exists");
        }

        Semester semester = Semester.builder()
                .schoolYear(schoolYear)
                .semesterType(request.semesterType())
                .build();

        semesterRepository.save(semester);
    }

    public SemesterResponse getSemesters(Long schoolYearId) {
        if (!schoolYearRepository.existsById(schoolYearId)) {
            throw new InvalidRequestException("School year does not exist");
        }

        List<SemesterResponse.SemesterDto> semesters = semesterRepository.findAllBySchoolYearIdOrderBySemesterTypeAsc(schoolYearId)
                .stream()
                .map((sem) -> new SemesterResponse.SemesterDto(
                        sem.getId(),
                        sem.getSemesterType()
                ))
                .toList();

        return new SemesterResponse(semesters);
    }

}
