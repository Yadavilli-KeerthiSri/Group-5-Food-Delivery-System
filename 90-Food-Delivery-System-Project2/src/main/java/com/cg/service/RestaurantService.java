package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.Restaurant;
import com.cg.iservice.IRestaurantService;
import com.cg.repository.RestaurantRepository;


@Service
public class RestaurantService implements IRestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public Restaurant add(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public Restaurant getById(Long id) {
        return restaurantRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }
}
