package com.raiffeisen.processor.repository.trigger;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentInsertTriggerTest {
    private ListAppender<ILoggingEvent> logWatcher;

    @Mock
    private Connection connection;

    @Mock
    private Statement statement;

    private final PaymentInsertTrigger paymentInsertTrigger = new PaymentInsertTrigger();

    @BeforeEach
    void setUp() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);
        logWatcher = new ListAppender<>();
        logWatcher.start();
        ((Logger) LoggerFactory.getLogger(PaymentInsertTrigger.class)).addAppender(logWatcher);
    }

    @Test
    void testInsertPayment() throws SQLException {
        paymentInsertTrigger.fire(connection, new Object[] {}, new Object[] { 1 });

        verify(statement).execute(anyString());
    }

    @Test
    void testInsertPaymentNotFound() throws SQLException {
        doThrow(new SQLException("Payment not found")).when(statement).execute(anyString());

        paymentInsertTrigger.fire(connection, new Object[] {}, new Object[] { 1 });

        assertAll(() -> assertEquals(1, logWatcher.list.size()),
                () -> assertEquals("Error while inserting payment audit for payment_id: {}, operation: INSERT",
                        logWatcher.list.getFirst().getMessage()));
    }

    @AfterEach
    void tearDown() {
        ((Logger) LoggerFactory.getLogger(PaymentInsertTrigger.class)).detachAndStopAllAppenders();
    }
}