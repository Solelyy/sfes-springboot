package com.sfes.registrar.records.schoolyear;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.records.schoolyear.dto.SchoolYearRequest;
import com.sfes.registrar.records.schoolyear.dto.SchoolYearResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolYearService {
    private final SchoolYearRepository schoolYearRepository;

    @Transactional
    public void createSchoolYear(SchoolYearRequest request){
        String normalizedSchoolYear = NormalizationUtil.normalizeSchoolYr(request.schoolYear());

        if (schoolYearRepository.findBySchoolYear(normalizedSchoolYear)
                .isPresent()
        ){
            throw new InvalidRequestException("School year already exist");
        }

        SchoolYear schoolYear = SchoolYear.builder()
                .schoolYear(normalizedSchoolYear)
                .build();

        schoolYearRepository.save(schoolYear);
    }

    public SchoolYearResponse getSchoolYears(){
        List<SchoolYearResponse.SchoolYearDto> schoolYears =
                schoolYearRepository.findAll(Sort.by(Sort.Direction.DESC, "schoolYear"))
                        .stream()
                        .map(sy -> new SchoolYearResponse.SchoolYearDto(
                                sy.getId(),
                                sy.getSchoolYear()
                        ))
                        .toList();

        return new SchoolYearResponse(schoolYears);
    }

    @Transactional
    public void updateSchoolYear(Long id, SchoolYearRequest request) {
        String normalizedSchoolYear = NormalizationUtil.normalizeSchoolYr(request.schoolYear());

        SchoolYear schoolYear = schoolYearRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("School year does not exist"));

        if (schoolYearRepository.findBySchoolYearAndIdNot(normalizedSchoolYear, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("School year already exist");
        }

        schoolYear.setSchoolYear(normalizedSchoolYear);
    }
}
