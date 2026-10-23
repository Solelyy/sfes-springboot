package com.sfes.registrar.subject;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.course.Course;
import com.sfes.registrar.course.CourseRepository;
import com.sfes.registrar.subject.dto.SubjectRequest;
import com.sfes.registrar.subject.dto.SubjectResponse;
import com.sfes.registrar.subject.dto.SubjectSearchResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public void addSubject(SubjectRequest request){
        String normalizedSubjectName = NormalizationUtil.normalizeToUpperCase(request.subjectName());
        String normalizedSubjectCode = NormalizationUtil.normalizeToUpperCase(request.subjectCode());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(request.courseCode());

        if (subjectRepository.findBySubjectName(normalizedSubjectName)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject name already exists");
        }

        if (subjectRepository.findBySubjectCode(normalizedSubjectCode)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject code already exists");
        }

        Course course = courseRepository.findByCourseCode(normalizedCourseCode)
                .orElseThrow(() -> new InvalidRequestException("Course does not exist"));

        Subject subject = Subject.builder()
                .subjectName(normalizedSubjectName)
                .subjectCode(normalizedSubjectCode)
                . course(course)
                .unitCount(request.unitCount())
                .build();

        subjectRepository.save(subject);
    }

    public SubjectResponse getSubjects(String departmentCode, int pageNumber, int size) {
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(departmentCode);

        Sort sort = Sort.by("subjectCode").ascending();

        Pageable pageable = PageRequest.of(pageNumber -1,size, sort);

        Page<Subject> result = subjectRepository.findSubjects(normalizedCourseCode, pageable);

        List<SubjectResponse.SubjectDto> subjects = result.stream()
                .map((subject) -> new SubjectResponse.SubjectDto(
                        subject.getId(),
                        subject.getSubjectName(),
                        subject.getSubjectCode(),
                        subject.getUnitCount(),
                        subject.getCourse().getCourseCode()
                ))
                .toList();

        return new SubjectResponse(
                subjects,
                new Meta(
                        result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }

    @Transactional
    public void updateSubject(Long id, SubjectRequest request) {
        String normalizedSubjectName = NormalizationUtil.normalizeToUpperCase(request.subjectName());
        String normalizedSubjectCode = NormalizationUtil.normalizeToUpperCase(request.subjectCode());
        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(request.courseCode());

        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestException("Subject does not exist"));

        if (subjectRepository
                .findBySubjectNameAndIdNot(normalizedSubjectName, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject name already exist");
        }

        if (subjectRepository
                .findBySubjectCodeAndIdNot(normalizedSubjectCode, id)
                .isPresent()
        ) {
            throw new InvalidRequestException("Subject code already exist");
        }

        Course course = courseRepository.findByCourseCode(normalizedCourseCode)
                .orElseThrow(() -> new InvalidRequestException("Course does not exist"));

        subject.setSubjectCode(normalizedSubjectCode);
        subject.setSubjectName(normalizedSubjectName);
        subject.setUnitCount(request.unitCount());
        subject.setCourse(course);
    }

    @Transactional(readOnly = true)
    public SubjectSearchResult getSearchResponses(
            Long semesterId,
            String searchItem
    ){
        String normalizedSearch = (searchItem == null || searchItem.isBlank())
                ? null
                : searchItem.trim();

        int pageSize = (normalizedSearch == null) ? 5 : 10;

        Pageable pageable = PageRequest.of(0, pageSize);

        List<SubjectSearchResult.SearchResultItem> results;

        if (normalizedSearch == null) {
            results = subjectRepository.initialSubjectsSearch(
                    semesterId,
                    pageable
            );
        } else {
            results = subjectRepository.findSubjectsSearchResult(
                    semesterId,
                    normalizedSearch,
                    pageable
            );
        }

        return new SubjectSearchResult(results);
    }
}