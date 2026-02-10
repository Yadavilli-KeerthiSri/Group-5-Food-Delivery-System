package com.cg.service;

import com.cg.dto.OrderDto;
import com.cg.entity.DeliveryAgent;
import com.cg.entity.MenuItem;
import com.cg.entity.Order;
import com.cg.enumeration.OrderStatus;
import com.cg.repository.CustomerRepository;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Super-simple tests for OrderService:
 * - minimal assertions
 * - basic verifies
 * - no captors, no heavy setup
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private DeliveryAgentRepository deliveryAgentRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private MenuItemRepository menuItemRepository;

    @InjectMocks
    private OrderService orderService;

    /* 1) place() -> saves order with items */
    @Test
    void place_basic_shouldSave() {
        OrderDto dto = new OrderDto();
        dto.setTotalAmount(120.0);
        dto.setItemIds(List.of(11L, 11L, 12L));

        // Minimal stubs
        MenuItem m11 = new MenuItem(); m11.setItemId(11L);
        MenuItem m12 = new MenuItem(); m12.setItemId(12L);
        when(menuItemRepository.findAllById(dto.getItemIds()))
                .thenReturn(List.of(m11, m12));

        Order saved = new Order();
        saved.setOrderId(1L);
        saved.setOrderStatus(OrderStatus.PLACED);
        saved.setTotalAmount(120.0);
        when(orderRepository.save(any(Order.class))).thenReturn(saved);

        OrderDto out = orderService.place(dto);

        assertNotNull(out);
        verify(menuItemRepository).findAllById(dto.getItemIds());
        verify(orderRepository).save(any(Order.class));
    }

    /* 2) updateStatus() from PLACED -> PREPARING */
    @Test
    void updateStatus_whenPlaced_shouldMoveToPreparing() {
        Long id = 1L;
        Order order = new Order();
        order.setOrderId(id);
        order.setOrderStatus(OrderStatus.PLACED);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(orderRepository.findByIdWithPaymentAndAgent(id)).thenReturn(Optional.of(order));

        orderService.updateStatus(id);

        assertEquals(OrderStatus.PREPARING, order.getOrderStatus());
        verify(orderRepository, atLeastOnce()).saveAndFlush(order);
        verify(orderRepository).findByIdWithPaymentAndAgent(id);
    }

    /* 3) updateStatus() from PREPARING with no agents -> throws */
    @Test
    void updateStatus_whenPreparingAndNoAgent_shouldThrow() {
        Long id = 2L;
        Order order = new Order();
        order.setOrderId(id);
        order.setOrderStatus(OrderStatus.PREPARING);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        when(deliveryAgentRepository.findFirstByAvailabilityTrue()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.updateStatus(id));

        verify(deliveryAgentRepository).findFirstByAvailabilityTrue();
        verify(orderRepository, never()).saveAndFlush(order);
    }

    /* 4) cancel() -> sets CANCELLED and releases agent if assigned */
    @Test
    void cancel_shouldSetCancelledAndReleaseAgent() {
        Long id = 3L;
        DeliveryAgent agent = new DeliveryAgent();
        agent.setAvailability(false);

        Order order = new Order();
        order.setOrderId(id);
        order.setOrderStatus(OrderStatus.PREPARING);
        order.setDeliveryAgent(agent);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));

        orderService.cancel(id);

        assertEquals(OrderStatus.CANCELLED, order.getOrderStatus());
        assertNull(order.getDeliveryAgent());
        assertTrue(agent.isAvailability());
        verify(deliveryAgentRepository).save(agent);
        verify(orderRepository).saveAndFlush(order);
    }
}
