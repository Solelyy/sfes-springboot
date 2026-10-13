package com.sfes.registrar.records.classoffering.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassOfferingRequest(
   @NotBlank
   @Size(min = 3, max = 25)
   String subjectCode
) {}
