package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.MenuItem;
import com.cg.entity.Restaurant;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.RestaurantRepository;

@Service
public class SearchService {
 
    @Autowired
    private RestaurantRepository restaurantRepository;
 
    @Autowired
    private MenuItemRepository menuItemRepository;
 
    public List<Restaurant> searchRestaurants(String keyword) {
        return restaurantRepository
                .findByRestaurantNameContainingIgnoreCase(keyword);
    }
 
    public List<MenuItem> searchMenuItems(String keyword) {
        return menuItemRepository
                .findByItemNameContainingIgnoreCase(keyword);
    }
}
 

