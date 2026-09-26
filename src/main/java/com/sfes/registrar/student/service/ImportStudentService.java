package com.sfes.registrar.student.service;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.CsvUtil;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.student.Student;
import com.sfes.registrar.student.StudentRepository;
import com.sfes.registrar.student.StudentStatus;
import com.sfes.registrar.student.dto.ImportStudentResponse;
import com.sfes.registrar.student.dto.StudentCsvRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ImportStudentService {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    private static final List<String> REQUIRED_HEADERS =
            List.of(
                    "Student ID",
                    "Email",
                    "First Name",
                    "Middle Name",
                    "Last Name",
                    "Department Code"
            );

    @Transactional
    public ImportStudentResponse importStudents(MultipartFile file) {
        CsvUtil.validateCsv(file);

        try {
            List<StudentCsvRecord> records = CsvUtil.parseCsv(
                    file,
                    REQUIRED_HEADERS,
                    StudentCsvRecord.class
            );

            List<StudentCsvRecord> normalizedRecords = records.stream()
                    .map(this::normalizedRecord)
                    .toList();

            return validateData(normalizedRecords);

        } catch (IOException e) {
            log.warn("Failed to parse CSV", e);
            throw new InvalidRequestException("Failed to parse CSV file");
        }
    }

    private StudentCsvRecord normalizedRecord(StudentCsvRecord record) {
        return new StudentCsvRecord(
                NormalizationUtil.normalizeStudentIdSafe(record.getStudentId()),
                NormalizationUtil.normalizeToLowerCase(record.getEmail()),
                NormalizationUtil.normalizeName(record.getFirstName()),
                NormalizationUtil.normalizeName(record.getMiddleName()),
                NormalizationUtil.normalizeName(record.getLastName()),
                NormalizationUtil.normalizeToUpperCase(record.getDepartmentCode())
        );
    }

    private ImportStudentResponse validateData(
            List<StudentCsvRecord> records
    ) {
        List<Student> validStudents = new ArrayList<>();
        List<ImportStudentResponse.FailedRow> failedRows = new ArrayList<>();

        Set<String> departmentCodesInCsv = records.stream()
                .map(StudentCsvRecord::getDepartmentCode)
                .filter(c -> c !=null && !c.isBlank())
                .collect(Collectors.toSet());

        Map<String, Department> deptByCode = departmentRepository
                .findByDepartmentCodeIn(departmentCodesInCsv).stream()
                .collect(Collectors.toMap(Department::getDepartmentCode, d-> d));

        Set<String> existingStudentIds = new HashSet<>(
                studentRepository.findExistingStudentIds(
                        records.stream()
                                .map(StudentCsvRecord::getStudentId)
                                .filter(e -> e != null && !e.isBlank())
                                .collect(Collectors.toSet())
                )
        );

        Set<String> existingEmails = new HashSet<>(
               studentRepository.findExistingEmails(
                        records.stream()
                                .map(StudentCsvRecord::getEmail)
                                .filter(e -> e != null && !e.isBlank())
                                .collect(Collectors.toSet())
                )
        );

        Set<String> studentIdsInCsv = new HashSet<>();

        Set<String> emailsInCsv = new HashSet<>();

        for (int i = 0; i < records.size(); i++) {
            StudentCsvRecord record = records.get(i);

            int rowNumber = i + 2;

            List<String> errors = new ArrayList<>();

            validateRequiredFields(record, errors);

            if(record.getStudentId() == null) {
                errors.add("Invalid student ID format");
            }

            if (record.getStudentId() != null && !record.getStudentId().isBlank()) {
                if (!studentIdsInCsv.add(record.getStudentId())) {
                    errors.add("Student ID is duplicated in the CSV");
                } else if (existingStudentIds.contains(record.getStudentId())){
                    errors.add("Student ID already exists");
                }
            }

            if (record.getEmail() != null && !record.getEmail().isBlank()) {
                if (!emailsInCsv.add(record.getEmail())) {
                    errors.add("Email is duplicated in the CSV");
                } else if (existingEmails.contains(record.getEmail())){
                    errors.add("Email already exists");
                }
            }

            Department dept = deptByCode.get(record.getDepartmentCode());
            if (dept == null) {
                errors.add("Department code does not exist");
            }

            if (!errors.isEmpty()) {
                failedRows.add(
                        new ImportStudentResponse.FailedRow(
                                rowNumber,
                                record.getStudentId(),
                                errors
                        )
                );

                continue;
            }
            Student student = Student.builder()
                    .studentId(record.getStudentId())
                    .email(record.getEmail())
                    .firstName(record.getFirstName())
                    .middleName(record.getMiddleName())
                    .lastName(record.getLastName())
                    .status(StudentStatus.ACTIVE)
                    .department(dept)
                    .build();

            validStudents.add(student);
        }

        studentRepository.saveAll(validStudents);

        int totalCount = records.size();
        int failedCount = failedRows.size();
        int successfulCount = validStudents.size();

        return new ImportStudentResponse(
                totalCount,
                successfulCount,
                failedCount,
                failedRows
        );
    }

    private void validateRequiredFields(
            StudentCsvRecord record,
            List<String> errors
    ){

        if (record.getStudentId() == null || record.getStudentId().isBlank()) {
            errors.add("Student ID is required");
        }

        if (record.getEmail() == null || record.getEmail().isBlank()) {
            errors.add("Email is required");
        }

        if (record.getFirstName() == null || record.getFirstName().isBlank()) {
            errors.add("First Name is required");
        }

        if (record.getLastName() == null || record.getLastName().isBlank()) {
            errors.add("Last Name is required");
        }

        if (record.getDepartmentCode() == null || record.getDepartmentCode().isBlank()) {
            errors.add("Department Code is required");
        }
    }
}