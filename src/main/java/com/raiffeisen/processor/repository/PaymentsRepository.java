package com.raiffeisen.processor.repository;

import com.raiffeisen.processor.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PaymentsRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
}
