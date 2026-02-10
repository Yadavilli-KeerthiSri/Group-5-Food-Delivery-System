package com.cg.service;

import com.cg.dto.MenuItemDto;
import com.cg.entity.MenuItem;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Super-simple tests for MenuItemService:
 * - Only basic asserts and verify()
 * - Positive + Negative coverage
 */
@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock private MenuItemRepository menuItemRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private OrderRepository orderRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    /* 1) POSITIVE: getById -> returns DTO when found */
    @Test
    void getById_shouldReturnDto_whenFound() {
        MenuItem entity = new MenuItem();
        entity.setItemId(10L);
        when(menuItemRepository.findById(10L)).thenReturn(Optional.of(entity));

        MenuItemDto out = menuItemService.getById(10L);

        assertNotNull(out);
        assertEquals(10L, out.getItemId());
        verify(menuItemRepository).findById(10L);
    }

    /* 2) NEGATIVE: update -> throws when item not found */
    @Test
    void update_shouldThrow_whenItemNotFound() {
        MenuItemDto dto = new MenuItemDto();
        dto.setItemId(99L);
        when(menuItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> menuItemService.update(dto));

        verify(menuItemRepository).findById(99L);
        verify(menuItemRepository, never()).save(any());
    }

    /* 3) NEGATIVE: delete -> throws when item is used in orders */
    @Test
    void delete_shouldThrow_whenItemUsedInOrders() {
        when(orderRepository.existsByItems_ItemId(5L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> menuItemService.delete(5L));

        verify(orderRepository).existsByItems_ItemId(5L);
        verify(menuItemRepository, never()).deleteById(anyLong());
    }

    /* 4) POSITIVE: delete -> calls repository when not used */
    @Test
    void delete_shouldDelete_whenNotUsedInOrders() {
        when(orderRepository.existsByItems_ItemId(6L)).thenReturn(false);

        menuItemService.delete(6L);

        verify(orderRepository).existsByItems_ItemId(6L);
        verify(menuItemRepository).deleteById(6L);
    }
}
