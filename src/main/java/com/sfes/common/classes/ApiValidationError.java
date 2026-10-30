package com.sfes.common.classes;

import java.util.List;

public record ApiValidationError(
        String message,
        List<String> errors
) {}
