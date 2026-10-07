package com.sfes.registrar.section;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.course.CourseRepository;
import com.sfes.registrar.section.dto.SectionRequest;
import com.sfes.registrar.section.dto.SectionResponse;
import com.sfes.registrar.yearlevel.YearLevel;
import com.sfes.registrar.yearlevel.YearLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final YearLevelRepository yearLevelRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void createSection(String courseCode, int yearNumber, SectionRequest request){
        String normalizedSectionName = NormalizationUtil.normalizeToUpperCase(request.sectionName());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(courseCode);

        YearLevel yearLevel = yearLevelRepository
                .findByYearNumberAndCourse_CourseCode(yearNumber, normalizedCourseCode)
                .orElseThrow(() -> new InvalidRequestException("Invalid course or year level"));

        if (sectionRepository
                .findBySectionNameAndYearLevelId(normalizedSectionName,yearLevel.getId())
                .isPresent()
        ){
            throw new InvalidRequestException("Section already exists");
        }

        Section section = Section.builder()
                .sectionName(normalizedSectionName)
                .yearLevel(yearLevel)
                .build();

        sectionRepository.save(section);
    }

    @Transactional(readOnly = true)
    public SectionResponse getSections(String courseCode, int yearNumber, int pageNumber, int pageSize) {
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(courseCode);

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by("sectionName").ascending()
        );

        Page<Section> result = sectionRepository
                .findSections(normalizedCourseCode, yearNumber, pageable);

        List<SectionResponse.SectionDto> sections = result.getContent()
                .stream()
                .map((section) -> new SectionResponse.SectionDto(
                        section.getId(),
                        section.getSectionName(),
                        section.getYearLevel().getYearNumber(),
                        section.getYearLevel().getCourse().getCourseCode()
                ))
                .toList();

        return new SectionResponse(
                sections,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    @Transactional
    public void updateSection(Long id, SectionRequest request) {
        String normalizedSectionName = NormalizationUtil.normalizeToUpperCase(request.sectionName());

        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Section does not exist"));

        if (sectionRepository
                .findBySectionNameAndYearLevelIdAndIdNot(
                        normalizedSectionName,
                        section.getYearLevel().getId(),
                        id
                ).isPresent()
        ) {
            throw new InvalidRequestException("Section already exist");
        }

        section.setSectionName(normalizedSectionName);
    }
}
