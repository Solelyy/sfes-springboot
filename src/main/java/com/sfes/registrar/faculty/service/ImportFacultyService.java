package com.sfes.registrar.faculty.service;

import com.sfes.common.exceptions.InvalidRequestException;
import com.sfes.common.utility.CsvUtil;
import com.sfes.common.utility.NormalizationUtil;
import com.sfes.registrar.Status;
import com.sfes.registrar.department.Department;
import com.sfes.registrar.department.DepartmentRepository;
import com.sfes.registrar.faculty.Faculty;
import com.sfes.registrar.faculty.FacultyRepository;
import com.sfes.registrar.faculty.dto.FacultyCsvRecord;
import com.sfes.registrar.faculty.dto.ImportFacultyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportFacultyService {
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    private static final List<String> REQUIRED_HEADERS =
            List.of(
                    "Employee ID",
                    "Email",
                    "First Name",
                    "Middle Name",
                    "Last Name",
                    "Department Code"
            );

    public ImportFacultyResponse importFaculty(MultipartFile file) {

        CsvUtil.validateCsv(file);

        try {
            List<FacultyCsvRecord> records = CsvUtil.parseCsv(
                            file,
                            REQUIRED_HEADERS,
                            FacultyCsvRecord.class
                    );

            List<FacultyCsvRecord> normalizedRecords = records.stream()
                    .map(this::normalizedRecord)
                    .toList();

             return validateData(normalizedRecords);
        } catch (IOException e) {
            log.debug("Failed to parse CSV: {}", e.getMessage());
            throw new InvalidRequestException("Failed to parse CSV file");
        }
    }


    private FacultyCsvRecord normalizedRecord(FacultyCsvRecord record) {
        return new FacultyCsvRecord(
                NormalizationUtil.normalizeToEmployeeId(record.getEmployeeId()),
                NormalizationUtil.normalizeToLowerCase(record.getEmail()),
                NormalizationUtil.normalizeName(record.getFirstName()),
                NormalizationUtil.normalizeName(record.getMiddleName()),
                NormalizationUtil.normalizeName(record.getLastName()),
                NormalizationUtil.normalizeToUpperCase(record.getDepartmentCode())
        );
    }

    private ImportFacultyResponse validateData(
            List<FacultyCsvRecord> records
    ) {
        List<Faculty> validFaculty = new ArrayList<>();
        List<ImportFacultyResponse.FailedRow> failedRows = new ArrayList<>();

        Set<String> employeeIdsInCsv = new HashSet<>();
        Set<String> emailsInCsv = new HashSet<>();

        for (int i = 0; i < records.size(); i++) {
            FacultyCsvRecord record = records.get(i);

            int rowNumber = i + 2;

            List<String> errors = new ArrayList<>();

            validateRequiredFields(record, errors);

            if (record.getEmployeeId() != null && !record.getEmployeeId().isBlank()) {
                if (!employeeIdsInCsv.add(record.getEmployeeId())) {
                    errors.add("Employee ID is duplicated in the CSV");
                } else if (facultyRepository
                        .findByEmployeeId(record.getEmployeeId())
                        .isPresent()) {

                    errors.add("Employee ID already exists");
                }
            }

            if (record.getEmail() != null && !record.getEmail().isBlank()) {
                if (!emailsInCsv.add(record.getEmail())) {
                    errors.add("Email is duplicated in the CSV");
                } else if (facultyRepository
                        .findByEmail(record.getEmail())
                        .isPresent()) {

                    errors.add("Email already exists");
                }
            }

            Optional<Department> department = Optional.empty();

            if (record.getDepartmentCode() != null
                    && !record.getDepartmentCode().isBlank()) {

                department = departmentRepository
                        .findByDepartmentCode(record.getDepartmentCode());

                if (department.isEmpty()) {
                    errors.add("Department code does not exist");
                }
            }

            if (!errors.isEmpty()) {
                failedRows.add(
                        new ImportFacultyResponse.FailedRow(
                                rowNumber,
                                record.getEmployeeId(),
                                errors
                        )
                );

                continue;
            }
            Faculty faculty = Faculty.builder()
                    .employeeId(record.getEmployeeId())
                    .email(record.getEmail())
                    .firstName(record.getFirstName())
                    .middleName(record.getMiddleName())
                    .lastName(record.getLastName())
                    .status(Status.ACTIVE)
                    .department(department.orElseThrow())
                    .build();

            validFaculty.add(faculty);
        }

        facultyRepository.saveAll(validFaculty);

        int totalCount = records.size();
        int failedCount = failedRows.size();
        int successfulCount = validFaculty.size();

        return new ImportFacultyResponse(
                totalCount,
                successfulCount,
                failedCount,
                failedRows
        );
    }

    private void validateRequiredFields(
            FacultyCsvRecord record,
            List<String> errors
    ){

        if (record.getEmployeeId() == null || record.getEmployeeId().isBlank()) {
            errors.add("Employee ID is required");
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
