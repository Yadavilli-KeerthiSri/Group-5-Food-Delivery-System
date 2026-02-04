package com.cg.service;

import com.cg.entity.Payment;
import com.cg.enumeration.TransactionStatus;
import com.cg.iservice.IPaymentService;
import com.cg.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment makePayment(Payment payment) {
        payment.setTransactionStatus(TransactionStatus.SUCCESS);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getById(Long id) {
        return paymentRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }
}
