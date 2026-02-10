package com.cg.service;

import com.cg.dto.DeliveryAgentDto;
import com.cg.entity.DeliveryAgent;
import com.cg.repository.DeliveryAgentRepository;
import com.cg.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Super-simple tests for DeliveryAgentService:
 * - Only basic asserts and verify()
 * - Positive + Negative coverage
 */
@ExtendWith(MockitoExtension.class)
class DeliveryAgentServiceTest {

    @Mock private DeliveryAgentRepository deliveryAgentRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private DeliveryAgentService deliveryAgentService;

    /* 1) POSITIVE: getById -> returns DTO when found */
    @Test
    void getById_shouldReturnDto_whenFound() {
        DeliveryAgent entity = new DeliveryAgent();
        entity.setAgentId(10L);
        when(deliveryAgentRepository.findById(10L)).thenReturn(Optional.of(entity));

        DeliveryAgentDto out = deliveryAgentService.getById(10L);

        assertNotNull(out);
        assertEquals(10L, out.getAgentId());
        verify(deliveryAgentRepository).findById(10L);
    }

    /* 2) NEGATIVE: getById -> throws when not found */
    @Test
    void getById_shouldThrow_whenNotFound() {
        when(deliveryAgentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> deliveryAgentService.getById(99L));

        verify(deliveryAgentRepository).findById(99L);
    }

    /* 3) NEGATIVE: update -> throws when agent not found */
    @Test
    void update_shouldThrow_whenAgentNotFound() {
        DeliveryAgentDto dto = new DeliveryAgentDto();
        dto.setAgentId(7L);
        when(deliveryAgentRepository.findById(7L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> deliveryAgentService.update(dto));

        verify(deliveryAgentRepository).findById(7L);
        verify(deliveryAgentRepository, never()).save(any());
    }

    /* 4) POSITIVE: delete -> calls repository deleteById */
    @Test
    void delete_shouldCallRepository() {
        deliveryAgentService.delete(5L);

        verify(deliveryAgentRepository).deleteById(5L);
    }
}
