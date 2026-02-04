package com.cg.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.entity.DeliveryAgent;
import com.cg.entity.Order;
import com.cg.enumeration.OrderStatus;
import com.cg.iservice.IOrderService;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.OrderRepository;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DeliveryAgentRepository deliveryAgentRepository;

    @Override
    @Transactional
    public Order place(Order order) {
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateStatus(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (order.getOrderStatus()) {
            case PLACED -> {
                orderRepository.updateStatusOnly(orderId, OrderStatus.PREPARING);
            }
            case PREPARING -> {
                DeliveryAgent agent = deliveryAgentRepository.findFirstByAvailabilityTrue()
                        .orElseThrow(() -> new RuntimeException("No agents available"));
                agent.setAvailability(false);
                deliveryAgentRepository.save(agent);
                
                // This query updates ONLY the order table
                orderRepository.updateStatusAndAgent(orderId, OrderStatus.PICKED_UP, agent);
            }
            case PICKED_UP -> orderRepository.updateStatusOnly(orderId, OrderStatus.OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY -> {
                orderRepository.updateStatusOnly(orderId, OrderStatus.DELIVERED);
                if (order.getDeliveryAgent() != null) {
                    DeliveryAgent agent = order.getDeliveryAgent();
                    agent.setAvailability(true);
                    deliveryAgentRepository.save(agent);
                }
            }
            default -> throw new IllegalStateException("Invalid status");
        }

        return orderRepository.findById(orderId).orElse(null);
    }

    @Override
    public void cancel(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public Order getById(Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Order> getByCustomer(Long customerId) {
        return orderRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }
    
    @Override
    public List<Order> getOrdersByCustomerEmail(String email) {
        List<Order> orders = orderRepository.findAllByCustomer_EmailOrderByOrderDateDesc(email);
        return (orders != null) ? orders : new ArrayList<>();
    }
}
