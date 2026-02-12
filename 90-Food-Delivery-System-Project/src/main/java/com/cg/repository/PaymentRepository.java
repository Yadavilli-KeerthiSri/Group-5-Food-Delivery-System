package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Payment;
import com.cg.enumeration.TransactionStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
	List<Payment> findByTransactionStatus(TransactionStatus status);
}
