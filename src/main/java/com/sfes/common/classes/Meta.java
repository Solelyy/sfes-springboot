package com.sfes.common.classes;

public record Meta (
        int currentPage,
        int limit,
        int totalPages,
        long totalElements
){}
