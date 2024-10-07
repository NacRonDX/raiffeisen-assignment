package com.raiffeisen.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
public class GetPaymentsFilterDto {
    private final BigDecimal amount;
    private final String currency;
    private final String fromAccount;
    private final String toAccount;
    private final ZonedDateTime fromTimestamp;
    private final ZonedDateTime toTimestamp;
}
