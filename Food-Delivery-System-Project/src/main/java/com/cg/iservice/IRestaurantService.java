package com.cg.iservice;

import java.util.List;

import com.cg.dto.RestaurantDto;

public interface IRestaurantService {

	RestaurantDto add(RestaurantDto dto);

	RestaurantDto update(RestaurantDto dto);

	RestaurantDto getById(Long id);

	List<RestaurantDto> getAll();

	List<RestaurantDto> findTopRated();

	void delete(Long restaurantId);

	List<RestaurantDto> findTopForDashboard();
}