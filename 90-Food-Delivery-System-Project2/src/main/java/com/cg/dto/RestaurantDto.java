package com.cg.dto;

import java.util.List;

public class RestaurantDto {
    private Long restaurantId;
    private String restaurantName;
    private String location;
    private String cuisine;
    private Double ratings;

    // Menu item IDs for this restaurant
    private List<Long> menuItemIds;

    public RestaurantDto() {}

    public RestaurantDto(Long restaurantId, String restaurantName, String location, String cuisine, Double ratings, List<Long> menuItemIds) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.location = location;
        this.cuisine = cuisine;
        this.ratings = ratings;
        this.menuItemIds = menuItemIds;
    }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public Double getRatings() { return ratings; }
    public void setRatings(Double ratings) { this.ratings = ratings; }

    public List<Long> getMenuItemIds() { return menuItemIds; }
    public void setMenuItemIds(List<Long> menuItemIds) { this.menuItemIds = menuItemIds; }
}