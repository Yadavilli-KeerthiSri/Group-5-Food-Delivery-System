package com.cg.mapper;

import java.util.List;
import java.util.function.Function;

import com.cg.dto.RestaurantDto;
import com.cg.entity.MenuItem;
import com.cg.entity.Restaurant;

public final class RestaurantMapper {

	private RestaurantMapper() {
	}

	// ===== Read =====
	public static RestaurantDto toDto(Restaurant e) {
		if (e == null)
			return null;
		RestaurantDto d = new RestaurantDto();
		d.setRestaurantId(e.getRestaurantId());
		d.setRestaurantName(e.getRestaurantName());
		d.setLocation(e.getLocation());
		d.setCuisine(e.getCuisine());
		d.setRatings(e.getRatings());
		d.setImageName(e.getImageName()); // <-- IMPORTANT
		// if you have relation to menu items, map ids here.
		return d;
	}

	// ===== Create =====
	public static Restaurant fromCreateDto(RestaurantDto d, Function<List<Long>, List<MenuItem>> loader) {
		if (d == null)
			return null;
		Restaurant entity = new Restaurant();
		entity.setRestaurantName(d.getRestaurantName());
		entity.setLocation(d.getLocation());
		entity.setCuisine(d.getCuisine());
		entity.setRatings(d.getRatings());
		entity.setImageName(d.getImageName()); // <-- IMPORTANT
		// if you have relation to menu items, load and set here.
		return entity;
	}

	// ===== Update =====
	public static void applyUpdate(RestaurantDto d, Restaurant e, Function<List<Long>, List<MenuItem>> loader) {
		if (d == null || e == null)
			return;
		if (d.getRestaurantName() != null)
			e.setRestaurantName(d.getRestaurantName());
		if (d.getLocation() != null)
			e.setLocation(d.getLocation());
		if (d.getCuisine() != null)
			e.setCuisine(d.getCuisine());
		if (d.getRatings() != null)
			e.setRatings(d.getRatings());
		if (d.getImageName() != null)
			e.setImageName(d.getImageName()); // <-- IMPORTANT
		// update menu items if needed
	}

	// ===== Delete helper =====
	public static Restaurant buildWithId(Long id) {
		if (id == null)
			return null;
		Restaurant e = new Restaurant();
		e.setRestaurantId(id);
		return e;
	}
}