package com.cg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;
import com.cg.enumeration.OrderStatus;
import com.cg.iservice.IOrderService;
import com.cg.mapper.OrderMapper;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;

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

    @Override
    @Transactional
    public OrderDto place(OrderDto dto) {
        // DTO -> Entity
        Order entity = OrderMapper.fromCreateDto(
                dto,
                cid -> customerRepository.findById(cid).orElse(null),
                aid -> deliveryAgentRepository.findById(aid).orElse(null),
                ids -> menuItemRepository.findAllById(ids)
        );
        entity.setOrderStatus(OrderStatus.PLACED);
        entity.setOrderDate(LocalDateTime.now());

        Order saved = orderRepository.save(entity);
        return OrderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderDto updateStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (order.getOrderStatus()) {
            case PLACED -> orderRepository.updateStatusOnly(orderId, OrderStatus.PREPARING);
            case PREPARING -> {
                DeliveryAgent agent = deliveryAgentRepository.findFirstByAvailabilityTrue()
                        .orElseThrow(() -> new RuntimeException("No agents available"));
                agent.setAvailability(false);
                deliveryAgentRepository.save(agent);
                orderRepository.updateStatusAndAgent(orderId, OrderStatus.PICKED_UP, agent);
            }
            case PICKED_UP -> orderRepository.updateStatusOnly(orderId, OrderStatus.OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY -> {
                // Explicitly set the new status
                order.setOrderStatus(OrderStatus.DELIVERED);
                
                // Handle Cash on Delivery Payment
                if (order.getPayment() != null && "CASH_ON_DELIVERY".equals(order.getPayment().toString())) {
                    order.setTransactionStatus(com.cg.enumeration.TransactionStatus.SUCCESS);
                }

                // Release the Agent
                if (order.getDeliveryAgent() != null) {
                    DeliveryAgent agent = order.getDeliveryAgent();
                    agent.setAvailability(true);
                    deliveryAgentRepository.save(agent);
                }
                
                // Save and force immediate database update
                orderRepository.saveAndFlush(order);
            }
            default -> throw new IllegalStateException("Invalid status transition from: " + order.getOrderStatus());
        }

        // Fetch fresh data for the DTO
        Order updated = orderRepository.findById(orderId).orElse(null);
        return OrderMapper.toDto(updated);
    }

    @Override
    public OrderDto getById(Long id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toDto)
                .orElseThrow();
    }

    @Override
    public List<OrderDto> getByCustomer(Long customerId) {
        return orderRepository.findByCustomerCustomerId(customerId)
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerEmail(String email) {
        List<Order> orders = orderRepository.findAllByCustomer_EmailOrderByOrderDateDesc(email);
        List<Order> safe = (orders != null) ? orders : new ArrayList<>();
        return safe.stream().map(OrderMapper::toDto).collect(Collectors.toList());
    }

	@Override
	public void cancel(Long orderId) {
		Order orderDto = orderRepository.findById(orderId).orElseThrow();
		orderDto.setOrderStatus(OrderStatus.CANCELLED);
		orderRepository.save(orderDto);
	}
}