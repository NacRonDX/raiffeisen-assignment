package com.raiffeisen.processor.repository.trigger;

import lombok.extern.slf4j.Slf4j;
import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class PaymentDeleteTrigger implements Trigger {
    @Override
    public void fire(Connection connection, Object[] objects, Object[] objects1) {
        try {
            connection.createStatement().execute("INSERT INTO payment_audit (payment_id, operation) VALUES (" + objects[0] + ", 'DELETE')");
        } catch (SQLException e) {
            log.error("Error while inserting payment audit for payment_id: {}, operation: DELETE", objects[0], e);
        }
    }
}
