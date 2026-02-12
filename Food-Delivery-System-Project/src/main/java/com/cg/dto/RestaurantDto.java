package com.cg.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class RestaurantDto {
	private Long restaurantId;

	@NotBlank(message = "{restaurant.name.notblank}")
	@Size(min = 2, max = 100, message = "{restaurant.name.size}")
	private String restaurantName;

	@NotBlank(message = "{restaurant.location.notblank}")
	@Size(min = 2, max = 120, message = "{restaurant.location.size}")
	private String location;

	@NotBlank(message = "{restaurant.cuisine.notblank}")
	@Size(min = 2, max = 80, message = "{restaurant.cuisine.size}")
	private String cuisine;

	@DecimalMin(value = "0.0", inclusive = true, message = "{restaurant.ratings.min}")
	@DecimalMax(value = "5.0", inclusive = true, message = "{restaurant.ratings.max}")
	private Double ratings;

	// Optional list of item IDs
	private List<@NotNull @Positive Long> menuItemIds;

	private String imageName;

	public RestaurantDto() {
	}

	public RestaurantDto(Long restaurantId, String restaurantName, String location, String cuisine, Double ratings,
			List<Long> menuItemIds, String imageName) {
		this.restaurantId = restaurantId;
		this.restaurantName = restaurantName;
		this.location = location;
		this.cuisine = cuisine;
		this.ratings = ratings;
		this.menuItemIds = menuItemIds;
		this.imageName = imageName;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCuisine() {
		return cuisine;
	}

	public void setCuisine(String cuisine) {
		this.cuisine = cuisine;
	}

	public Double getRatings() {
		return ratings;
	}

	public void setRatings(Double ratings) {
		this.ratings = ratings;
	}

	public List<Long> getMenuItemIds() {
		return menuItemIds;
	}

	public void setMenuItemIds(List<Long> menuItemIds) {
		this.menuItemIds = menuItemIds;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}