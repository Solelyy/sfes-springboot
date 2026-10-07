package com.sfes.registrar.section.dto;

import jakarta.validation.constraints.*;

public record SectionRequest(
        @NotBlank
        @Size(min = 4, max = 25)
        String sectionName
) {}
