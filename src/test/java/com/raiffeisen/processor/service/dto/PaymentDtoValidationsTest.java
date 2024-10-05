package com.raiffeisen.processor.service.dto;

import com.raiffeisen.processor.dto.PaymentDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentDtoValidationsTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testAmountNullValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(null)
                .currency("USD")
                .fromAccount("12345678901234567890")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Amount cannot be null")))
        );
    }

    @Test
    void testAmountNegativeValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("-1"))
                .currency("USD")
                .fromAccount("12345678901234567890")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Amount must be a non-zero positive value")))
        );
    }

    @Test
    void testCurrencyEmptyValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("")
                .fromAccount("12345678901234567890")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Currency cannot be empty")))
        );
    }

    @Test
    void testCurrencyPatternValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("usd")
                .fromAccount("12345678901234567890")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Currency value does not match the pattern")))
        );
    }

    @Test
    void testFromAccountPatternValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("USD")
                .fromAccount("123456789012345678901")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Account number does not match the pattern")))
        );
    }

    @Test
    void testFromAccountEmptyValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("USD")
                .fromAccount("")
                .toAccount("09876543210987654321")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Account number cannot be empty")))
        );
    }

    @Test
    void testToAccountPatternValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("USD")
                .fromAccount("12345678901234567890")
                .toAccount("098765432109876543210")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Account number does not match the pattern")))
        );
    }

    @Test
    void testToAccountEmptyValidation() {
        var paymentDto = PaymentDto.builder()
                .amount(new BigDecimal("1"))
                .currency("USD")
                .fromAccount("12345678901234567890")
                .toAccount("")
                .build();

        var violations = validator.validate(paymentDto);

        assertAll(
                () -> assertFalse(violations.isEmpty()),
                () -> assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Account number cannot be empty")))
        );
    }
}
