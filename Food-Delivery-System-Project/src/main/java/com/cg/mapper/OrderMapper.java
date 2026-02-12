package com.cg.mapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.MenuItem;
import com.cg.entity.Order;
import com.cg.entity.Payment;

public final class OrderMapper {
	private OrderMapper() {
	}

	// ===== Read =====
	public static OrderDto toDto(Order entity) {
		if (entity == null)
			return null;
		OrderDto dto = new OrderDto();
		dto.setOrderId(entity.getOrderId());
		dto.setOrderDate(entity.getOrderDate());
		dto.setOrderStatus(entity.getOrderStatus());
		dto.setTotalAmount(entity.getTotalAmount());

		// Customer
		dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getCustomerId() : null);

		// Delivery Agent (ID + Name + Phone)
		DeliveryAgent agent = entity.getDeliveryAgent();
		if (agent != null) {
			dto.setDeliveryAgentId(agent.getAgentId());
			dto.setDeliveryAgentName(agent.getAgentName());
			dto.setDeliveryAgentPhone(agent.getContact());
		} else {
			dto.setDeliveryAgentId(null);
			dto.setDeliveryAgentName(null);
			dto.setDeliveryAgentPhone(null);
		}

		// Items -> ids (you use duplicates to represent quantity)
		List<Long> itemIds = entity.getItems() == null ? null
				: entity.getItems().stream().filter(Objects::nonNull).map(MenuItem::getItemId)
						.collect(Collectors.toList());
		dto.setItemIds(itemIds);

		// Payment fields
		Payment payment = entity.getPayment();
		if (payment != null) {
			dto.setPaymentId(payment.getPaymentId());
			dto.setPaymentMethod(payment.getPaymentMethod());
			if (payment.getTransactionStatus() != null) {
				dto.setTransactionStatus(payment.getTransactionStatus());
			} else {
				dto.setTransactionStatus(entity.getTransactionStatus());
			}
		} else {
			dto.setPaymentId(null);
			dto.setPaymentMethod(null);
			dto.setTransactionStatus(entity.getTransactionStatus());
		}

		return dto;
	}

	// ===== Create =====
	public static Order fromCreateDto(OrderDto dto,
			java.util.function.Function<Long, com.cg.entity.Customer> customerResolver,
			java.util.function.Function<Long, com.cg.entity.DeliveryAgent> deliveryAgentResolver,
			java.util.function.Function<List<Long>, List<com.cg.entity.MenuItem>> menuItemsResolver) {

		if (dto == null)
			return null;
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
	public static void applyUpdate(OrderDto dto, Order target,
			java.util.function.Function<Long, com.cg.entity.Customer> customerResolver,
			java.util.function.Function<Long, com.cg.entity.DeliveryAgent> deliveryAgentResolver,
			java.util.function.Function<List<Long>, List<com.cg.entity.MenuItem>> menuItemsResolver) {

		if (dto == null || target == null)
			return;
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
		if (id == null)
			return null;
		Order e = new Order();
		e.setOrderId(id);
		return e;
	}
}
