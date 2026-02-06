package com.cg.mapper;

import java.util.function.Function;

import com.cg.dto.PaymentDto;
import com.cg.entity.Order;
import com.cg.entity.Payment;

public final class PaymentMapper {

    private PaymentMapper() {}

    // ===== Read =====
    public static PaymentDto toDto(Payment entity) {
        if (entity == null) return null;
        PaymentDto dto = new PaymentDto();
        dto.setPaymentId(entity.getPaymentId());
        dto.setPaymentMethod(entity.getPaymentMethod());
        dto.setAmount(entity.getAmount());
        dto.setTransactionStatus(entity.getTransactionStatus());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getOrderId() : null);
        return dto;
    }

    // ===== Create =====
    public static Payment fromCreateDto(
            PaymentDto dto,
            Function<Long, Order> orderResolver
    ) {
        if (dto == null) return null;
        Payment entity = new Payment();
        entity.setPaymentId(dto.getPaymentId());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setAmount(dto.getAmount());
        entity.setTransactionStatus(dto.getTransactionStatus());

        if (dto.getOrderId() != null && orderResolver != null) {
            entity.setOrder(orderResolver.apply(dto.getOrderId()));
        }
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(
            PaymentDto dto,
            Payment target,
            Function<Long, Order> orderResolver
    ) {
        if (dto == null || target == null) return;
        target.setPaymentMethod(dto.getPaymentMethod());
        target.setAmount(dto.getAmount());
        target.setTransactionStatus(dto.getTransactionStatus());

        if (dto.getOrderId() != null && orderResolver != null) {
            target.setOrder(orderResolver.apply(dto.getOrderId()));
        }
    }

    // ===== Delete helper =====
    public static Payment buildWithId(Long id) {
        if (id == null) return null;
        Payment e = new Payment();
        e.setPaymentId(id);
        return e;
    }
}