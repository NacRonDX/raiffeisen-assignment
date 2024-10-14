package com.raiffeisen.processor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Builder
@Getter
@Setter
@ToString
public class PaymentDto {
    private Long id;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a non-zero positive value")
    private BigDecimal amount;
    @NotEmpty(message = "Currency cannot be empty")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency value does not match the pattern")
    private String currency;
    // placeholder for account number (IBAN)
    @Pattern(regexp = "^\\d{20}$", message = "Account number does not match the pattern")
    @NotEmpty(message = "Account number cannot be empty")
    private String fromAccount;
    @Pattern(regexp = "^\\d{20}$", message = "Account number does not match the pattern")
    @NotEmpty(message = "Account number cannot be empty")
    private String toAccount;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ZonedDateTime timestamp;
}
