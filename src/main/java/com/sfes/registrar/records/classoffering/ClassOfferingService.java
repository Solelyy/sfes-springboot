package com.sfes.registrar.records.classoffering;

import com.sfes.common.classes.Meta;
import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.records.classoffering.dto.ClassOfferingRequest;
import com.sfes.registrar.records.classoffering.dto.ClassOfferingResponse;
import com.sfes.registrar.records.semester.Semester;
import com.sfes.registrar.records.semester.SemesterRepository;
import com.sfes.registrar.subject.Subject;
import com.sfes.registrar.subject.SubjectRepository;
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
public class ClassOfferingService {
    private final ClassOfferingRepository classOfferingRepository;
    private final SubjectRepository subjectRepository;
    private final SemesterRepository semesterRepository;

    @Transactional
    public void createClassOffering(Long semesterId, ClassOfferingRequest request){
        String normalizedSubjCode = NormalizationUtil.normalizeToUpperCase(request.subjectCode());

        Subject subject = subjectRepository.findBySubjectCode(normalizedSubjCode)
                .orElseThrow(() -> new InvalidRequestException("Subject does not exist"));

        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new InvalidRequestException("Semester does not exist"));

        if (classOfferingRepository
                .findBySubjectIdAndSemesterId(subject.getId(), semester.getId())
                .isPresent()
        ) {
            throw new InvalidRequestException("Class offering already exists for this subject and semester");
        }

        ClassOffering classOffering = ClassOffering.builder()
                .semester(semester)
                .subject(subject)
                .build();

        classOfferingRepository.save(classOffering);
    }

    public ClassOfferingResponse getClassOfferings(
            String courseCode,
            Long semesterId,
            int pageNumber,
            int pageSize
    ) {

        String normalizedCourseCode = NormalizationUtil.normalizeToUpperCase(courseCode);

        Pageable pageable = PageRequest.of(
                pageNumber - 1,
                pageSize,
                Sort.by("subject.subjectCode").ascending()
        );

        Page<ClassOffering> result = classOfferingRepository.findClassOfferings(normalizedCourseCode, semesterId, pageable);

        List<ClassOfferingResponse.ClassOfferingDto> classOfferings = result.getContent()
                .stream()
                .map((classOffering) -> new ClassOfferingResponse.ClassOfferingDto(
                        classOffering.getId(),
                        classOffering.getSubject().getSubjectCode(),
                        classOffering.getSubject().getSubjectName()
                ))
                .toList();

        return new ClassOfferingResponse(
                classOfferings,
                new Meta(result.getNumber() + 1,
                        result.getSize(),
                        result.getTotalPages(),
                        result.getTotalElements()
                )
        );
    }
}
