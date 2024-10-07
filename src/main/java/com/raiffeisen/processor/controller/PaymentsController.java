package com.raiffeisen.processor.controller;

import com.raiffeisen.processor.constants.SortField;
import com.raiffeisen.processor.dto.GetPaymentsFilterDto;
import com.raiffeisen.processor.dto.PageSpecDto;
import com.raiffeisen.processor.dto.PaymentDto;
import com.raiffeisen.processor.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentService paymentService;

    @GetMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentDto>> getPayments(@RequestParam(required = false) final BigDecimal amount,
                                                        @RequestParam(required = false) final String currency,
                                                        @RequestParam(required = false) final String fromAccount,
                                                        @RequestParam(required = false) final String toAccount,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime fromTimestamp,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final ZonedDateTime toTimestamp,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "5") int sizePerPage,
                                                        @RequestParam(defaultValue = "ID") SortField sortField,
                                                        @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection) {
        var filter = new GetPaymentsFilterDto(amount, currency, fromAccount, toAccount, fromTimestamp, toTimestamp);
        var pageSpec = new PageSpecDto(page, sizePerPage, sortField, sortDirection);
        return ResponseEntity.ok(paymentService.getPayments(filter, pageSpec));
    }

    @PostMapping(consumes = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createPayment(@Valid @RequestBody final PaymentDto paymentDto) {
        paymentService.createPayment(paymentDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable final Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok().build();
    }
}
