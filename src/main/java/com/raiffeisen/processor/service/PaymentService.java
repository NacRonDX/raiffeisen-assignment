package com.raiffeisen.processor.service;

import com.raiffeisen.processor.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getPayments();
    void createPayment(PaymentDto paymentDto);
    void deletePayment(Long id);
}
