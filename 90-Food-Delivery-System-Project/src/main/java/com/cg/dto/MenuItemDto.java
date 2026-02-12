package com.cg.dto;

import jakarta.validation.constraints.*;

public class MenuItemDto {
	private Long itemId;

	@NotBlank(message = "{item.name.notblank}")
	@Size(min = 2, max = 80, message = "{item.name.size}")
	private String itemName;

	@NotBlank(message = "{item.category.notblank}")
	@Size(min = 2, max = 40, message = "{item.category.size}")
	private String category;

	@NotNull(message = "{item.price.notnull}")
	@Positive(message = "{item.price.positive}")
	@Digits(integer = 8, fraction = 2, message = "{item.price.digits}")
	private Double price;

	// Optional. If you require at least one image name, add @NotBlank
	@Size(max = 1000, message = "{item.imageNames.size}")
	private String imageNames;

	@NotNull(message = "{item.restaurantId.notnull}")
	@Positive(message = "{item.restaurantId.positive}")
	private Long restaurantId;

	public MenuItemDto() {
	}

	public MenuItemDto(Long itemId, String itemName, String category, Double price, String imageNames,
			Long restaurantId) {
		this.itemId = itemId;
		this.itemName = itemName;
		this.category = category;
		this.price = price;
		this.imageNames = imageNames;
		this.restaurantId = restaurantId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImageNames() {
		return imageNames;
	}

	public void setImageNames(String imageNames) {
		this.imageNames = imageNames;
	}

	public Long getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
}