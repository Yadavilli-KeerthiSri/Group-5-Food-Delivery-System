package com.cg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;
import com.cg.entity.Payment;
import com.cg.enumeration.OrderStatus;
import com.cg.iservice.IOrderService;
import com.cg.mapper.OrderMapper;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.entity.MenuItem;

@Service
public class OrderService implements IOrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DeliveryAgentRepository deliveryAgentRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	/* =========================================
	   PLACE ORDER
	   ========================================= */
	@Override
	@Transactional
	public OrderDto place(OrderDto dto) {
	    Order entity = new Order();
	    entity.setOrderStatus(dto.getOrderStatus() != null ? dto.getOrderStatus() : OrderStatus.PLACED);
	    entity.setOrderDate(LocalDateTime.now());
	    entity.setTotalAmount(dto.getTotalAmount());

	    // Set customer
	    if (dto.getCustomerId() != null) {
	        customerRepository.findById(dto.getCustomerId()).ifPresent(entity::setCustomer);
	    }

	    // Add items from itemIds (duplicates represent quantity)
	    if (dto.getItemIds() != null && !dto.getItemIds().isEmpty()) {
	        List<MenuItem> itemsToAdd = new ArrayList<>();

	        // Load unique items once
	        List<MenuItem> allItems = menuItemRepository.findAllById(dto.getItemIds());
	        Map<Long, MenuItem> itemById = allItems.stream()
	                .collect(Collectors.toMap(MenuItem::getItemId, it -> it));

	        // Rebuild with duplicates
	        for (Long itemId : dto.getItemIds()) {
	            MenuItem item = itemById.get(itemId);
	            if (item != null) {
	                itemsToAdd.add(item);
	            }
	        }
	        entity.setItems(itemsToAdd);
	    }

	    Order saved = orderRepository.save(entity);
	    return OrderMapper.toDto(saved);
	}

	/* =========================================
	   UPDATE STATUS (Agent assignment fixed)
	   ========================================= */
	@Override
	@Transactional
	public OrderDto updateStatus(Long orderId) {
	    Order order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new RuntimeException("Order not found"));

	    switch (order.getOrderStatus()) {

	        case PLACED -> {
	            order.setOrderStatus(OrderStatus.PREPARING);
	            orderRepository.saveAndFlush(order);
	        }

	        case PREPARING -> {
	            // Pick an available agent
	            DeliveryAgent agent = deliveryAgentRepository.findFirstByAvailabilityTrue()
	                    .orElseThrow(() -> new RuntimeException("No agents available"));

	            // Reserve agent
	            agent.setAvailability(false);
	            deliveryAgentRepository.save(agent);

	            // Assign to this order and change status to PICKED_UP
	            order.setDeliveryAgent(agent);
	            order.setOrderStatus(OrderStatus.PICKED_UP);

	            // Persist both changes atomically
	            orderRepository.saveAndFlush(order);
	        }

	        case PICKED_UP -> {
	            order.setOrderStatus(OrderStatus.OUT_FOR_DELIVERY);
	            orderRepository.saveAndFlush(order);
	        }

	        case OUT_FOR_DELIVERY -> {
	            order.setOrderStatus(OrderStatus.DELIVERED);

	            // Handle Cash on Delivery Payment
	            if (order.getPayment() != null
	                    && order.getPayment().getPaymentMethod() == com.cg.enumeration.PaymentMethod.CASH_ON_DELIVERY) {
	                order.setTransactionStatus(com.cg.enumeration.TransactionStatus.SUCCESS);
	            }

	            // Release the Agent
	            if (order.getDeliveryAgent() != null) {
	                DeliveryAgent agent = order.getDeliveryAgent();
	                agent.setAvailability(true);
	                deliveryAgentRepository.save(agent);
	            }

	            orderRepository.saveAndFlush(order);
	        }

	        default -> throw new IllegalStateException("Invalid status transition from: " + order.getOrderStatus());
	    }

	    // Re-fetch with payment + agent for a consistent DTO
	    Order updated = orderRepository.findByIdWithPaymentAndAgent(orderId)
	            .orElseThrow(() -> new RuntimeException("Order not found after update"));

	    return OrderMapper.toDto(updated);
	}

	/* =========================================
	   GET ONE (Details page)
	   ========================================= */
	@Override
	@Transactional(readOnly = true)
	public OrderDto getById(Long id) throws NotFoundException {
	    Order order = orderRepository.findByIdWithPaymentAndAgent(id)
	            .orElseThrow(NotFoundException::new);

	    OrderDto dto = OrderMapper.toDto(order);

	    // Build item details (name -> quantity/price/subtotal)
	    if (order.getItems() != null && !order.getItems().isEmpty()) {
	        Map<String, OrderDto.OrderItemDetail> itemDetailsMap = new LinkedHashMap<>();
	        Map<String, Integer> itemQuantities = new HashMap<>();
	        Map<String, Double> itemPrices = new HashMap<>();

	        // Count items by name (duplicates represent quantity)
	        for (MenuItem item : order.getItems()) {
	            String itemName = item.getItemName();
	            itemQuantities.put(itemName, itemQuantities.getOrDefault(itemName, 0) + 1);
	            // Store unit price once
	            itemPrices.putIfAbsent(itemName, item.getPrice());
	        }

	        // Build detail map
	        itemQuantities.forEach((itemName, qty) -> {
	            double unitPrice = itemPrices.get(itemName);
	            double subtotal = qty * unitPrice;
	            itemDetailsMap.put(itemName, new OrderDto.OrderItemDetail(qty, unitPrice, subtotal));
	        });

	        dto.setItemDetails(itemDetailsMap);
	    }

	    return dto;
	}

	/* =========================================
	   LISTS
	   ========================================= */
	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> getByCustomer(Long customerId) {
	    return orderRepository.findByCustomerCustomerId(customerId)
	            .stream()
	            .map(OrderMapper::toDto)
	            .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> getAll() {
	    return orderRepository.findAll()
	            .stream()
	            .map(OrderMapper::toDto)
	            .collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<OrderDto> getOrdersByCustomerEmail(String email) {
	    List<Order> orders = orderRepository.findAllByCustomer_EmailOrderByOrderDateDesc(email);
	    List<Order> safe = (orders != null) ? orders : new ArrayList<>();
	    return safe.stream().map(OrderMapper::toDto).collect(Collectors.toList());
	}

	/* =========================================
	   CANCEL
	   ========================================= */
	@Override
	@Transactional
	public void cancel(Long orderId) {
	    Order order = orderRepository.findById(orderId).orElseThrow();
	    order.setOrderStatus(OrderStatus.CANCELLED);

	    // If an agent was already assigned, release them
	    if (order.getDeliveryAgent() != null) {
	        DeliveryAgent agent = order.getDeliveryAgent();
	        agent.setAvailability(true);
	        deliveryAgentRepository.save(agent);
	        order.setDeliveryAgent(null);
	    }

	    orderRepository.saveAndFlush(order);
	}

	/* =========================================
	   CREATE ORDER (simple)
	   ========================================= */
	@Override
	@Transactional
	public OrderDto createOrder(OrderDto newOrderDto) {
	    Order order = new Order();
	    order.setTotalAmount(newOrderDto.getTotalAmount());
	    order.setOrderDate(LocalDateTime.now());
	    order.setOrderStatus(OrderStatus.PENDING);

	    // Link customer if provided
	    if (newOrderDto.getCustomerId() != null) {
	        customerRepository.findById(newOrderDto.getCustomerId())
	                .ifPresent(order::setCustomer);
	    }

	    Order savedOrder = orderRepository.save(order);
	    return OrderMapper.toDto(savedOrder);
	}

	/* =========================================
	   AGENTS
	   ========================================= */
	@Override
	@Transactional(readOnly = true)
	public List<DeliveryAgent> getAvailableAgents() {
	    return deliveryAgentRepository.findAllByAvailabilityTrue();
	}

	/* =========================================
	   OPTIONAL: map helper (kept for compatibility)
	   ========================================= */
	@Override
	public OrderDto map(Order order) {
	    OrderDto dto = new OrderDto();
	    dto.setOrderId(order.getOrderId());
	    dto.setOrderDate(order.getOrderDate());
	    dto.setOrderStatus(order.getOrderStatus());
	    dto.setTotalAmount(order.getTotalAmount());

	    if (order.getCustomer() != null) {
	        dto.setCustomerId(order.getCustomer().getCustomerId());
	    }

	    if (order.getItems() != null) {
	        dto.setItemIds(order.getItems().stream().map(MenuItem::getItemId).toList());
	    }

	    Payment payment = order.getPayment();
	    if (payment != null) {
	        dto.setPaymentId(payment.getPaymentId());
	        dto.setPaymentMethod(payment.getPaymentMethod());
	        if (payment.getTransactionStatus() != null) {
	            dto.setTransactionStatus(payment.getTransactionStatus());
	        }
	    } else {
	        dto.setTransactionStatus(order.getTransactionStatus());
	    }

	    DeliveryAgent agent = order.getDeliveryAgent();
	    if (agent != null) {
	        dto.setDeliveryAgentId(agent.getAgentId());
	        dto.setDeliveryAgentName(agent.getAgentName());
	        dto.setDeliveryAgentPhone(agent.getContact());
	    }

	    return dto;
	}
}