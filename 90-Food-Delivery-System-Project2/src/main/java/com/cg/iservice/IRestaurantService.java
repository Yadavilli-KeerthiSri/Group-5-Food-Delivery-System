package com.cg.iservice;

import java.util.List;

import com.cg.entity.Restaurant;

public interface IRestaurantService {
	Restaurant add(Restaurant restaurant);

    Restaurant update(Restaurant restaurant);

    Restaurant getById(Long id);

    List<Restaurant> getAll();

    void delete(Long id);
}
