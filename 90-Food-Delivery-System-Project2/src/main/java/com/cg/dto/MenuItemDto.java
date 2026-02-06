package com.cg.dto;

public class MenuItemDto {
    private Long itemId;
    private String itemName;
    private String category;
    private Double price;
    private String imageNames;

    // Reference to restaurant
    private Long restaurantId;

    public MenuItemDto() {}

    public MenuItemDto(Long itemId, String itemName, String category, Double price, String imageName, Long restaurantId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.price = price;
        this.imageNames = imageNames;
        this.restaurantId = restaurantId;
    }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public String getImageNames() { return imageNames; }
    public void setImageNames(String imageNames) { this.imageNames = imageNames; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}