package com.raiffeisen.processor.service.impl;

import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.service.mapper.PaymentMapper;
import com.raiffeisen.processor.repository.PaymentsRepository;
import com.raiffeisen.processor.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentsRepository paymentsRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentDto> getPayments() {
        return paymentsRepository.findAll().stream()
                .map(paymentMapper::toDto)
                .toList();
    }

    @Override
    public void createPayment(final PaymentDto paymentDto) {
        var payment = paymentMapper.toEntity(paymentDto);
        payment.setTimestamp(ZonedDateTime.now());
        paymentsRepository.save(payment);
    }

    @Override
    public void deletePayment(final Long id) {
        paymentsRepository.deleteById(id);
    }
}
