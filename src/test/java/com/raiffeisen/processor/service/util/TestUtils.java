package com.raiffeisen.processor.service.util;

import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.entity.Payment;

import java.math.BigDecimal;

public final class TestUtils {
    public static Payment generatePayment() {
        return Payment.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(150.98))
                .currency("USD")
                .fromAccount("123")
                .toAccount("456")
                .timestamp(null)
                .build();
    }

    public static PaymentDto generatePaymentDto() {
        return PaymentDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(150.98))
                .currency("USD")
                .fromAccount("123")
                .toAccount("456")
                .timestamp(null)
                .build();
    }
}
