package com.raiffeisen.processor.service;

import com.raiffeisen.processor.dto.GetPaymentsFilterDto;
import com.raiffeisen.processor.dto.PageSpecDto;
import com.raiffeisen.processor.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getPayments(GetPaymentsFilterDto filter, PageSpecDto pageSpec);

    void createPayment(PaymentDto paymentDto);

    void deletePayment(Long id);
}
