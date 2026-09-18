package com.sfes.common.utility;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.sfes.common.exceptions.InvalidRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class CsvUtil {
    private CsvUtil() {}

    public static void validateCsv(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isEmpty() || !fileName.toLowerCase(Locale.ROOT).endsWith(".csv")){
            throw new InvalidRequestException("Supported file is CSV");
        }

        if (file.isEmpty()) {
            throw new InvalidRequestException("Please select a valid CSV file to upload");
        }
    }

    public static <T> List<T> parseCsv(
            MultipartFile file,
            List <String> requiredHeaders,
            Class<T> type
    ) throws IOException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {

            String[] headerLine = csvReader.peek();

            if (headerLine == null) {
                throw new InvalidRequestException("CSV file is empty");
            }

            if (headerLine.length <= 1) {
                throw new InvalidRequestException("Invalid CSV formatting");
            }

            Set<String> header = Arrays.stream(headerLine)
                    .map(String::trim)
                    .map(s -> s.toUpperCase(Locale.ROOT))
                    .collect(Collectors.toSet());

            List<String> missingHeaders = requiredHeaders.stream()
                    .filter(required -> !header.contains(required.toUpperCase(Locale.ROOT)))
                    .toList();

            if (!missingHeaders.isEmpty()) {
                throw new InvalidRequestException("Missing required headers: " + missingHeaders);
            }

            HeaderColumnNameMappingStrategy<T> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(type);

            CsvToBean<T> csvToBean =
                    new CsvToBeanBuilder<T>(csvReader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();

            return csvToBean.parse();
        }
    }
}
