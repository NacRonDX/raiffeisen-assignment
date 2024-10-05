package com.raiffeisen.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@ToString
public class ErrorResponseDto {
    private final ZonedDateTime timestamp;
    private final String message;
    private final Integer status;
    private final String error;
}
