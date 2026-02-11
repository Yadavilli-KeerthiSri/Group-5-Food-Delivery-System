package com.cg.service;

import com.cg.dto.RestaurantDto;
import com.cg.entity.Restaurant;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Super-simple tests for RestaurantService: - Only basic asserts and verify() -
 * Positive + Negative coverage
 */
@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private MenuItemRepository menuItemRepository;
	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private RestaurantService restaurantService;

	/* 1) POSITIVE: getById -> returns DTO when found */
	@Test
	void getById_shouldReturnDto_whenFound() {
		Restaurant entity = new Restaurant();
		entity.setRestaurantId(10L);
		when(restaurantRepository.findById(10L)).thenReturn(Optional.of(entity));

		RestaurantDto out = restaurantService.getById(10L);

		assertNotNull(out);
		assertEquals(10L, out.getRestaurantId());
		verify(restaurantRepository).findById(10L);
	}

	/* 2) NEGATIVE: getById -> throws when not found */
	@Test
	void getById_shouldThrow_whenNotFound() {
		when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> restaurantService.getById(99L));

		verify(restaurantRepository).findById(99L);
	}

	/* 3) NEGATIVE: delete -> throws when restaurant has items used in orders */
	@Test
	void delete_shouldThrow_whenRestaurantUsedInOrders() {
		when(orderRepository.existsByItems_Restaurant_RestaurantId(5L)).thenReturn(true);

		assertThrows(IllegalStateException.class, () -> restaurantService.delete(5L));

		verify(orderRepository).existsByItems_Restaurant_RestaurantId(5L);
		verify(restaurantRepository, never()).deleteById(anyLong());
	}

	/* 4) POSITIVE: delete -> calls repository when not used in orders */
	@Test
	void delete_shouldDelete_whenNotUsedInOrders() {
		when(orderRepository.existsByItems_Restaurant_RestaurantId(6L)).thenReturn(false);

		restaurantService.delete(6L);

		verify(orderRepository).existsByItems_Restaurant_RestaurantId(6L);
		verify(restaurantRepository).deleteById(6L);
	}
}
