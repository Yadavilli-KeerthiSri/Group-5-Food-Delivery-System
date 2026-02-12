package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.RestaurantDto;
import com.cg.entity.Restaurant;
import com.cg.iservice.IRestaurantService;
import com.cg.mapper.RestaurantMapper;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.repository.RestaurantRepository;

@Service
public class RestaurantService implements IRestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public RestaurantDto add(RestaurantDto dto) {
		Restaurant entity = RestaurantMapper.fromCreateDto(dto, ids -> menuItemRepository.findAllById(ids));
		Restaurant saved = restaurantRepository.save(entity);
		return RestaurantMapper.toDto(saved);
	}

	@Override
	public RestaurantDto update(RestaurantDto dto) {
		// Using getRestaurantId() as per your DTO structure
		Restaurant existing = restaurantRepository.findById(dto.getRestaurantId())
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));

		RestaurantMapper.applyUpdate(dto, existing, ids -> menuItemRepository.findAllById(ids));

		Restaurant saved = restaurantRepository.save(existing);
		return RestaurantMapper.toDto(saved);
	}

	@Override
	public RestaurantDto getById(Long id) {
		return restaurantRepository.findById(id).map(RestaurantMapper::toDto)
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));
	}

	@Override
	public List<RestaurantDto> getAll() {
		List<RestaurantDto> out = restaurantRepository.findAll().stream().map(RestaurantMapper::toDto)
				.collect(Collectors.toList());
		// DEBUG: verify imageName flows (remove later)
		out.forEach(r -> System.out.println("DTO imageName = " + r.getImageName()));
		return out;
	}

	@Override
	public List<RestaurantDto> findTopRated() {
		// Original logic: Fetch ratings >= 4.0, limited to top 6
		return restaurantRepository.findTop6ByRatingsGreaterThanEqualOrderByRatingsDesc(4.0).stream()
				.map(RestaurantMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<RestaurantDto> findTopForDashboard() {
		List<Restaurant> top = restaurantRepository.findTop6ByOrderByRatingsDesc();
		if (top == null || top.isEmpty()) {
			top = restaurantRepository.findTopRatedRestaurants();
		}
		List<RestaurantDto> out = top.stream().map(RestaurantMapper::toDto).collect(Collectors.toList());
		// DEBUG (remove later)
		out.forEach(r -> System.out.println("DASHBOARD DTO imageName = " + r.getImageName()));
		return out;
	}

	@Override
	@Transactional
	public void delete(Long restaurantId) {
		if (orderRepository.existsByItems_Restaurant_RestaurantId(restaurantId)) {
			throw new IllegalStateException("Cannot delete: this restaurant has items used in orders.");
		}
		restaurantRepository.deleteById(restaurantId);
	}

	// Alias for the dashboard method as seen in your previous code
	public List<RestaurantDto> getTopRatedRestaurants() {
		return findTopForDashboard();
	}
}
