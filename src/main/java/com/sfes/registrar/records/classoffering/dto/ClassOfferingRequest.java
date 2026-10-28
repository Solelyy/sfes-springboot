package com.sfes.registrar.records.classoffering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassOfferingRequest(
   @NotBlank(message = "Subject code is required")
   @Size(min = 3, max = 25, message = "Subject code must be between 3 and 25 characters")
   String subjectCode
) {}
