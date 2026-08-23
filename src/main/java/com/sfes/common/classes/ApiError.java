package com.sfes.common.classes;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
