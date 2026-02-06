package com.cg.mapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cg.dto.DeliveryAgentDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;

public final class DeliveryAgentMapper {

    private DeliveryAgentMapper() {}

    // ===== Read =====
    public static DeliveryAgentDto toDto(DeliveryAgent entity) {
        if (entity == null) return null;
        DeliveryAgentDto dto = new DeliveryAgentDto();
        dto.setAgentId(entity.getAgentId());
        dto.setAgentName(entity.getAgentName());
        dto.setContact(entity.getContact());
        dto.setVehicleDetails(entity.getVehicleDetails());
        dto.setAvailability(entity.isAvailability());

        List<Long> orderIds = entity.getOrders() == null ? null :
            entity.getOrders().stream()
                  .filter(Objects::nonNull)
                  .map(Order::getOrderId)
                  .collect(Collectors.toList());
        dto.setOrderIds(orderIds);
        return dto;
    }

    // ===== Create =====
    public static DeliveryAgent fromCreateDto(
            DeliveryAgentDto dto,
            Function<List<Long>, List<Order>> orderResolver
    ) {
        if (dto == null) return null;
        DeliveryAgent entity = new DeliveryAgent();
        entity.setAgentId(dto.getAgentId());
        entity.setAgentName(dto.getAgentName());
        entity.setContact(dto.getContact());
        entity.setVehicleDetails(dto.getVehicleDetails());
        entity.setAvailability(dto.isAvailability());

        if (dto.getOrderIds() != null && orderResolver != null) {
            entity.setOrders(orderResolver.apply(dto.getOrderIds()));
        }
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(
            DeliveryAgentDto dto,
            DeliveryAgent target,
            Function<List<Long>, List<Order>> orderResolver
    ) {
        if (dto == null || target == null) return;
        target.setAgentName(dto.getAgentName());
        target.setContact(dto.getContact());
        target.setVehicleDetails(dto.getVehicleDetails());
        target.setAvailability(dto.isAvailability());

        if (dto.getOrderIds() != null && orderResolver != null) {
            target.setOrders(orderResolver.apply(dto.getOrderIds()));
        }
    }

    // ===== Delete helper =====
    public static DeliveryAgent buildWithId(Long id) {
        if (id == null) return null;
        DeliveryAgent e = new DeliveryAgent();
        e.setAgentId(id);
        return e;
    }
}
