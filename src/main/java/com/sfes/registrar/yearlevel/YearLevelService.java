
package com.sfes.registrar.yearlevel;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.course.CourseRepository;
import com.sfes.registrar.yearlevel.dto.YearLevelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YearLevelService {

    private final YearLevelRepository yearLevelRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public YearLevelResponse getYearLevels(String courseCode) {
        String normalizedCourseCode =
                NormalizationUtil.normalizeToUpperCase(courseCode);

        courseRepository.findByCourseCode(normalizedCourseCode)
                .orElseThrow(() ->
                        new InvalidRequestException(
                                "Course does not exist"
                        )
                );

        List<YearLevelResponse.YearLevelDto> yearLevels =
                yearLevelRepository
                        .findByCourse_CourseCodeAndStatusOrderByYearNumberAsc(
                                normalizedCourseCode,
                                Status.ACTIVE
                        )
                        .stream()
                        .map(y -> new YearLevelResponse.YearLevelDto(
                                y.getId(),
                                y.getYearNumber()
                        ))
                        .toList();

        return new YearLevelResponse(yearLevels);
    }
}
