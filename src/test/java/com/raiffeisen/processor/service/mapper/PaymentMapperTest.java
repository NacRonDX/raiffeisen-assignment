package com.raiffeisen.processor.service.mapper;

import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.entity.Payment;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMapperTest {
    private final PaymentMapper paymentMapper = new PaymentMapper();

    @Test
    void testToDtoWithValidInput() {
        var payment = Payment.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100.50))
                .currency("USD")
                .fromAccount("12345")
                .toAccount("67890")
                .timestamp(ZonedDateTime.now())
                .build();

        var paymentDto = paymentMapper.toDto(payment);

        assertAll(
                () -> assertEquals(payment.getId(), paymentDto.getId()),
                () -> assertEquals(payment.getAmount(), paymentDto.getAmount()),
                () -> assertEquals(payment.getCurrency(), paymentDto.getCurrency()),
                () -> assertEquals(payment.getFromAccount(), paymentDto.getFromAccount()),
                () -> assertEquals(payment.getToAccount(), paymentDto.getToAccount()),
                () -> assertEquals(payment.getTimestamp(), paymentDto.getTimestamp())
        );
    }

    @Test
    void testToEntityWithValidInput() {
        var paymentDto = PaymentDto.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(100.50))
                .currency("USD")
                .fromAccount("12345")
                .toAccount("67890")
                .timestamp(ZonedDateTime.now())
                .build();

        var payment = paymentMapper.toEntity(paymentDto);

        assertAll(
                () -> assertEquals(paymentDto.getId(), payment.getId()),
                () -> assertEquals(paymentDto.getAmount(), payment.getAmount()),
                () -> assertEquals(paymentDto.getCurrency(), payment.getCurrency()),
                () -> assertEquals(paymentDto.getFromAccount(), payment.getFromAccount()),
                () -> assertEquals(paymentDto.getToAccount(), payment.getToAccount()),
                () -> assertEquals(paymentDto.getTimestamp(), payment.getTimestamp())
        );
    }
}