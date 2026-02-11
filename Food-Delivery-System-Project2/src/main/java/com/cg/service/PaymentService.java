package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.PaymentDto;
import com.cg.entity.Order;
import com.cg.entity.Payment;
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
    @Transactional
    public void makePayment(PaymentDto dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setTransactionStatus(dto.getTransactionStatus());
        
        // Link to existing Order Entity
        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        payment.setOrder(order);
        
        paymentRepository.save(payment);
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