package com.sfes.common.classes;

public record ApiResponse<T>(
        String message,
        T data
) {}
