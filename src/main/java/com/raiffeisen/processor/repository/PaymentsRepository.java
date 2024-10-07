package com.raiffeisen.processor.repository;

import com.raiffeisen.processor.entity.Payment;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findById(Long id);
}
