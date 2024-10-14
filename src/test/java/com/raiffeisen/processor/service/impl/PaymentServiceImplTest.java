package com.raiffeisen.processor.service.impl;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.raiffeisen.processor.constants.SortField;
import com.raiffeisen.processor.dto.GetPaymentsFilterDto;
import com.raiffeisen.processor.dto.PageSpecDto;
import com.raiffeisen.processor.entity.Payment;
import com.raiffeisen.processor.exception.DataNotFoundException;
import com.raiffeisen.processor.repository.PaymentsRepository;
import com.raiffeisen.processor.service.mapper.PaymentMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.raiffeisen.processor.service.util.TestUtils.generatePayment;
import static com.raiffeisen.processor.service.util.TestUtils.generatePaymentDto;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    private ListAppender<ILoggingEvent> logWatcher;

    @Mock
    private PaymentsRepository paymentsRepository;
    @Spy
    private PaymentMapper paymentMapper;
    @Mock
    private Page<Object> page;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Captor
    private ArgumentCaptor<Payment> paymentEntityCaptor;

    @BeforeEach
    void setup() {
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(PaymentServiceImpl.class)).addAppender(logWatcher);
    }

    @Test
    void testGetAllPayments() {
        when(page.stream()).thenReturn(Stream.of(generatePayment(), generatePayment()));
        when(paymentsRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
        var filter = new GetPaymentsFilterDto(null, null, null, null, null, null);
        var pageSpec = new PageSpecDto(0, 2, SortField.ID, Sort.Direction.DESC);

        var payments = paymentService.getPayments(filter, pageSpec);

        verify(paymentMapper, times(2)).toDto(any(Payment.class));
        assertAll(() -> assertEquals(2, payments.size()), () -> assertEquals(2, logWatcher.list.size()),
                () -> assertEquals("Getting all payments", logWatcher.list.getFirst().getFormattedMessage()),
                () -> assertEquals("Found 2 payments", logWatcher.list.getLast().getFormattedMessage()));
    }

    @ParameterizedTest
    @CsvSource({ "150.98, USD, 123, 456, 2021-10-01T00:00:00Z, 2021-10-02T00:00:00Z",
            "0, USD, 123, 456, 2021-10-01T00:00:00Z, 2021-10-02T00:00:00Z",
            "150.98, null, 123, 456, 2021-10-01T00:00:00Z, 2021-10-02T00:00:00Z",
            "150.98, USD, null, 456, 2021-10-01T00:00:00Z, 2021-10-02T00:00:00Z",
            "150.98, USD, 123, null, 2021-10-01T00:00:00Z, 2021-10-02T00:00:00Z",
            "0, null, null, null, 2021-10-01T00:00:00Z, 2021-10-01T00:00:00Z" })
    void testGetAllPaymentsFilter(final BigDecimal amount, final String currency, final String fromAccount,
            final String toAccount, final ZonedDateTime fromTimestamp, final ZonedDateTime toTimestamp) {
        when(page.stream()).thenReturn(Stream.of(generatePayment(), generatePayment()));
        when(paymentsRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(page);
        var filter = new GetPaymentsFilterDto(amount, currency, fromAccount, toAccount, fromTimestamp, toTimestamp);
        var pageSpec = new PageSpecDto(0, 2, SortField.ID, Sort.Direction.DESC);

        var payments = paymentService.getPayments(filter, pageSpec);

        verify(paymentMapper, times(2)).toDto(any(Payment.class));
        assertAll(() -> assertEquals(2, payments.size()), () -> assertEquals(2, logWatcher.list.size()),
                () -> assertEquals("Getting all payments", logWatcher.list.getFirst().getFormattedMessage()),
                () -> assertEquals("Found 2 payments", logWatcher.list.getLast().getFormattedMessage()));
    }

    @Test
    void testCreatePayment() {
        var payment = generatePaymentDto();

        paymentService.createPayment(payment);

        verify(paymentMapper).toEntity(payment);
        verify(paymentsRepository).save(paymentEntityCaptor.capture());
        var timestamp = paymentEntityCaptor.getValue().getTimestamp();
        assertAll(() -> assertEquals(2, logWatcher.list.size()), () -> assertEquals(
                "Creating payment: PaymentDto(id=1, amount=150.98, currency=USD, fromAccount=123, toAccount=456, timestamp=null)",
                logWatcher.list.getFirst().getFormattedMessage()),
                () -> assertEquals(MessageFormatter.format(
                        "Payment created: Payment(id=1, amount=150.98, currency=USD, fromAccount=123, toAccount=456, timestamp={})",
                        timestamp).getMessage(), logWatcher.list.getLast().getFormattedMessage()));
    }

    @Test
    void testDeletePaymentWhenItExists() {
        var id = 1L;
        when(paymentsRepository.findById(id)).thenReturn(Optional.of(generatePayment()));

        paymentService.deletePayment(id);

        verify(paymentsRepository).findById(id);
        assertAll(() -> assertEquals(2, logWatcher.list.size()),
                () -> assertEquals("Deleting payment with id: 1", logWatcher.list.getFirst().getFormattedMessage()),
                () -> assertEquals("Payment with id 1 deleted", logWatcher.list.getLast().getFormattedMessage()));
    }

    @Test
    void testDeletePaymentWhenItDoesNotExistThrowException() {
        var id = 1L;
        when(paymentsRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> paymentService.deletePayment(id));

        verify(paymentsRepository).findById(id);
        assertAll(() -> assertEquals(2, logWatcher.list.size()),
                () -> assertEquals("Deleting payment with id: 1", logWatcher.list.getFirst().getFormattedMessage()),
                () -> assertEquals("Payment with id 1 does not exist",
                        logWatcher.list.getLast().getFormattedMessage()));
    }

    @AfterEach
    void tearDown() {
        ((Logger) LoggerFactory.getLogger(PaymentServiceImpl.class)).detachAndStopAllAppenders();
    }
}