package com.raiffeisen.processor.service.mapper;

import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.entity.Payment;
import com.raiffeisen.processor.service.Mapper;
import org.springframework.stereotype.Service;

@Service
public class PaymentMapper implements Mapper<PaymentDto, Payment> {
    public PaymentDto toDto(final Payment payment) {
        return PaymentDto.builder().id(payment.getId()).amount(payment.getAmount()).currency(payment.getCurrency())
                .fromAccount(payment.getFromAccount()).toAccount(payment.getToAccount())
                .timestamp(payment.getTimestamp()).build();
    }

    public Payment toEntity(final PaymentDto paymentDto) {
        return Payment.builder().id(paymentDto.getId()).amount(paymentDto.getAmount())
                .currency(paymentDto.getCurrency()).fromAccount(paymentDto.getFromAccount())
                .toAccount(paymentDto.getToAccount()).timestamp(paymentDto.getTimestamp()).build();
    }
}
