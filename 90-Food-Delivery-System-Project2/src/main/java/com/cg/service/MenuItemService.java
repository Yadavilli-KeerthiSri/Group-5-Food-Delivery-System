package com.cg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.entity.MenuItem;
import com.cg.iservice.IMenuItemService;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.RestaurantRepository;

@Service
public class MenuItemService implements IMenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Override
    public MenuItem add(MenuItem item) {
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItem update(MenuItem item) {
        return menuItemRepository.save(item);
    }

    @Override
    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id).orElseThrow();
    }

    @Override
    public List<MenuItem> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantRestaurantId(restaurantId);
    }

    @Override
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }

    @Override
	public List<MenuItem> getAll() {
		return menuItemRepository.findAll();
	}
}
