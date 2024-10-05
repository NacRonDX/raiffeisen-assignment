package com.raiffeisen.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class ErrorResponseDto {
    private final ZonedDateTime timestamp;
    private final List<String> details;
    private final Integer status;
    private final String error;
}
