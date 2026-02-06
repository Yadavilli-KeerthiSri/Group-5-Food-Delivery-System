package com.cg.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.dto.MenuItemDto;
import com.cg.dto.RestaurantDto;
import com.cg.entity.MenuItem;
import com.cg.entity.Restaurant;
import com.cg.mapper.MenuItemMapper;
import com.cg.mapper.RestaurantMapper;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.RestaurantRepository;

@Service
public class SearchService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    public List<RestaurantDto> searchRestaurants(String keyword) {
        List<Restaurant> found = restaurantRepository
                .findByRestaurantNameContainingIgnoreCase(keyword);
        return found.stream().map(RestaurantMapper::toDto).collect(Collectors.toList());
    }

    public List<MenuItemDto> searchMenuItems(String keyword) {
        List<MenuItem> found = menuItemRepository
                .findByItemNameContainingIgnoreCase(keyword);
        return found.stream().map(MenuItemMapper::toDto).collect(Collectors.toList());
    }
}