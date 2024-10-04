package com.raiffeisen.processor.repository;

import com.raiffeisen.processor.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {
}
