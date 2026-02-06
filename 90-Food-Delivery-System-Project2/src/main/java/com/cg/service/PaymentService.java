package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.PaymentDto;
import com.cg.entity.Payment;
import com.cg.enumeration.TransactionStatus;
import com.cg.iservice.IPaymentService;
import com.cg.mapper.PaymentMapper;
import com.cg.repository.OrderRepository;
import com.cg.repository.PaymentRepository;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentDto makePayment(PaymentDto dto) {
        Payment entity = PaymentMapper.fromCreateDto(
                dto,
                oid -> orderRepository.findById(oid).orElse(null)
        );
        entity.setTransactionStatus(TransactionStatus.SUCCESS);
        Payment saved = paymentRepository.save(entity);
        return PaymentMapper.toDto(saved);
    }

    @Override
    public PaymentDto getById(Long id) {
        return paymentRepository.findById(id)
                .map(PaymentMapper::toDto)
                .orElseThrow();
    }

    @Override
    public List<PaymentDto> getAll() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentMapper::toDto)
                .collect(Collectors.toList());
    }
}