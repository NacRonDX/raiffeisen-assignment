package com.raiffeisen.processor.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortField {
    ID("id"), AMOUNT("amount"), CURRENCY("currency"), FROM_ACCOUNT("fromAccount"), TO_ACCOUNT("toAccount"),
    TIMESTAMP("timestamp");

    private final String fieldName;
}
