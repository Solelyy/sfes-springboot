package com.sfes.registrar.section.dto;

import jakarta.validation.constraints.*;

public record SectionRequest(
        @NotBlank(message = "Section name is required")
        @Size(min = 4, max = 25, message = "Section name must be between 4 and 25 characters")
        String sectionName
) {}
