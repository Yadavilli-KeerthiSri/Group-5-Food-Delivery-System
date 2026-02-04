package com.cg.iservice;

import java.util.List;

import com.cg.entity.MenuItem;

public interface IMenuItemService {
	MenuItem add(MenuItem item);

    MenuItem update(MenuItem item);

    MenuItem getById(Long id);

    List<MenuItem> getByRestaurant(Long restaurantId);

    void delete(Long id);
    
    public List<MenuItem> getAll();
}
