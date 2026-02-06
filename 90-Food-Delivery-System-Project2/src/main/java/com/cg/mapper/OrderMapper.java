package com.cg.mapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cg.dto.OrderDto;
import com.cg.entity.Customer;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.MenuItem;
import com.cg.entity.Order;

public final class OrderMapper {

    private OrderMapper() {}

    // ===== Read =====
    public static OrderDto toDto(Order entity) {
        if (entity == null) return null;
        OrderDto dto = new OrderDto();
        dto.setOrderId(entity.getOrderId());
        dto.setOrderDate(entity.getOrderDate());
        dto.setOrderStatus(entity.getOrderStatus());
        dto.setTotalAmount(entity.getTotalAmount());
        dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null);
        dto.setDeliveryAgentId(entity.getDeliveryAgent() != null ? entity.getDeliveryAgent().getAgentId() : null);

        List<Long> itemIds = entity.getItems() == null ? null :
            entity.getItems().stream()
                  .filter(Objects::nonNull)
                  .map(MenuItem::getItemId)
                  .collect(Collectors.toList());
        dto.setItemIds(itemIds);

        dto.setPaymentId(entity.getPayment() != null ? entity.getPayment().getPaymentId() : null);
        return dto;
    }

    // ===== Create =====
    public static Order fromCreateDto(
            OrderDto dto,
            Function<Long, Customer> customerResolver,
            Function<Long, DeliveryAgent> deliveryAgentResolver,
            Function<List<Long>, List<MenuItem>> menuItemsResolver
    ) {
        if (dto == null) return null;
        Order entity = new Order();
        entity.setOrderId(dto.getOrderId());
        entity.setOrderDate(dto.getOrderDate());
        entity.setOrderStatus(dto.getOrderStatus());
        entity.setTotalAmount(dto.getTotalAmount());

        if (dto.getCustomerId() != null && customerResolver != null) {
            entity.setCustomer(customerResolver.apply(dto.getCustomerId()));
        }
        if (dto.getDeliveryAgentId() != null && deliveryAgentResolver != null) {
            entity.setDeliveryAgent(deliveryAgentResolver.apply(dto.getDeliveryAgentId()));
        }
        if (dto.getItemIds() != null && menuItemsResolver != null) {
            entity.setItems(menuItemsResolver.apply(dto.getItemIds()));
        }
        // Payment is 1:1 from Payment side; don't set here.
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(
            OrderDto dto,
            Order target,
            Function<Long, Customer> customerResolver,
            Function<Long, DeliveryAgent> deliveryAgentResolver,
            Function<List<Long>, List<MenuItem>> menuItemsResolver
    ) {
        if (dto == null || target == null) return;
        target.setOrderDate(dto.getOrderDate());
        target.setOrderStatus(dto.getOrderStatus());
        target.setTotalAmount(dto.getTotalAmount());
        if (dto.getCustomerId() != null && customerResolver != null) {
            target.setCustomer(customerResolver.apply(dto.getCustomerId()));
        }
        if (dto.getDeliveryAgentId() != null && deliveryAgentResolver != null) {
            target.setDeliveryAgent(deliveryAgentResolver.apply(dto.getDeliveryAgentId()));
        }
        if (dto.getItemIds() != null && menuItemsResolver != null) {
            target.setItems(menuItemsResolver.apply(dto.getItemIds()));
        }
    }

    // ===== Delete helper =====
    public static Order buildWithId(Long id) {
        if (id == null) return null;
        Order e = new Order();
        e.setOrderId(id);
        return e;
    }
}