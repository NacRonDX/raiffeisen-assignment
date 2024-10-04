package com.raiffeisen.processor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
public class PaymentDto {
    private Long id;
    @NotNull
    @Positive
    private BigDecimal amount;
    @NotEmpty
    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;
    //placeholder for account number (IBAN)
    @Pattern(regexp = "^\\d{20}$")
    @NotEmpty
    private String fromAccount;
    @Pattern(regexp = "^\\d{20}$")
    @NotEmpty
    private String toAccount;
    private ZonedDateTime timestamp;
}
