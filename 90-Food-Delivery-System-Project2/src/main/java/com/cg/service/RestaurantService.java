package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.RestaurantDto;
import com.cg.entity.Restaurant;
import com.cg.iservice.IRestaurantService;
import com.cg.mapper.RestaurantMapper;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.RestaurantRepository;

@Service
public class RestaurantService implements IRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Override
    public RestaurantDto add(RestaurantDto dto) {
        Restaurant entity = RestaurantMapper.fromCreateDto(
                dto,
                ids -> menuItemRepository.findAllById(ids)
        );
        Restaurant saved = restaurantRepository.save(entity);
        return RestaurantMapper.toDto(saved);
    }

    @Override
    public RestaurantDto update(RestaurantDto dto) {
        // Using getRestaurantId() as per your DTO structure
        Restaurant existing = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        
        RestaurantMapper.applyUpdate(
                dto,
                existing,
                ids -> menuItemRepository.findAllById(ids)
        );
        
        Restaurant saved = restaurantRepository.save(existing);
        return RestaurantMapper.toDto(saved);
    }

    @Override
    public RestaurantDto getById(Long id) {
        return restaurantRepository.findById(id)
                .map(RestaurantMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @Override
    public List<RestaurantDto> getAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantDto> findTopRated() {
        // Original logic: Fetch ratings >= 4.0, limited to top 6
        return restaurantRepository.findTop6ByRatingsGreaterThanEqualOrderByRatingsDesc(4.0)
                .stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Logic brought back from your previous snippet: 
     * Tries the derived query first, falls back to native query if empty.
     */
    public List<RestaurantDto> findTopForDashboard() {
        List<Restaurant> top = restaurantRepository.findTop6ByOrderByRatingsDesc();
        
        if (top == null || top.isEmpty()) {
            top = restaurantRepository.findTopRatedRestaurants(); // native query call
        }
        
        return top.stream()
                .map(RestaurantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    // Alias for the dashboard method as seen in your previous code
    public List<RestaurantDto> getTopRatedRestaurants() {
        return findTopForDashboard();
    }
}
