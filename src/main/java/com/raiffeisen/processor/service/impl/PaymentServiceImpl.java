package com.raiffeisen.processor.service.impl;

import com.raiffeisen.processor.dto.GetPaymentsFilterDto;
import com.raiffeisen.processor.dto.PageSpecDto;
import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.exception.DataNotFoundException;
import com.raiffeisen.processor.repository.PaymentsRepository;
import com.raiffeisen.processor.repository.specification.PaymentSpecification;
import com.raiffeisen.processor.service.PaymentService;
import com.raiffeisen.processor.service.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentsRepository paymentsRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public List<PaymentDto> getPayments(final GetPaymentsFilterDto filter, final PageSpecDto pageSpec) {
        log.info("Getting all payments");
        var specification = PaymentSpecification.getPaymentsByFilter(filter);
        var pageRequest = PageRequest.of(pageSpec.getPage(), pageSpec.getSizePerPage(),
                pageSpec.getSortDirection(), pageSpec.getSortField().getFieldName());
        var payments = paymentsRepository.findAll(specification, pageRequest)
                .stream()
                .map(paymentMapper::toDto)
                .toList();
        log.info("Found {} payments", payments.size());
        return payments;
    }

    @Override
    public void createPayment(final PaymentDto paymentDto) {
        log.info("Creating payment: {}", paymentDto);
        var payment = paymentMapper.toEntity(paymentDto);
        payment.setTimestamp(ZonedDateTime.now());
        paymentsRepository.save(payment);
        log.info("Payment created: {}", payment);
    }

    @Override
    @Transactional
    public void deletePayment(final Long id) {
        log.info("Deleting payment with id: {}", id);
        if (paymentsRepository.existsById(id)) {
            paymentsRepository.deleteById(id);
            log.info("Payment with id {} deleted", id);
        } else {
            log.warn("Payment with id {} does not exist", id);
            throw new DataNotFoundException("Payment with id " + id + " does not exist");
        }
    }
}
