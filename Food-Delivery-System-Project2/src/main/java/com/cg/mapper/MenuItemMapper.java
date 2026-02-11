package com.cg.mapper;

import java.util.function.Function;

import com.cg.dto.MenuItemDto;
import com.cg.entity.MenuItem;
import com.cg.entity.Restaurant;

public final class MenuItemMapper {

    private MenuItemMapper() {}

    // ===== Read =====
    public static MenuItemDto toDto(MenuItem entity) {
        if (entity == null) return null;
        MenuItemDto dto = new MenuItemDto();
        dto.setItemId(entity.getItemId());
        dto.setItemName(entity.getItemName());
        dto.setCategory(entity.getCategory());
        dto.setPrice(entity.getPrice());
        dto.setImageNames(entity.getImageNames());
        dto.setRestaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getRestaurantId() : null);
        return dto;
    }

    // ===== Create =====
    public static MenuItem fromCreateDto(
            MenuItemDto dto,
            Function<Long, Restaurant> restaurantResolver
    ) {
        if (dto == null) return null;
        MenuItem entity = new MenuItem();
        entity.setItemId(dto.getItemId());
        entity.setItemName(dto.getItemName());
        entity.setCategory(dto.getCategory());
        entity.setPrice(dto.getPrice());
        entity.setImageNames(dto.getImageNames());

        if (dto.getRestaurantId() != null && restaurantResolver != null) {
            entity.setRestaurant(restaurantResolver.apply(dto.getRestaurantId()));
        }
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(
            MenuItemDto dto,
            MenuItem target,
            Function<Long, Restaurant> restaurantResolver
    ) {
        if (dto == null || target == null) return;
        target.setItemName(dto.getItemName());
        target.setCategory(dto.getCategory());
        target.setPrice(dto.getPrice());
        target.setImageNames(dto.getImageNames());
        if (dto.getRestaurantId() != null && restaurantResolver != null) {
            target.setRestaurant(restaurantResolver.apply(dto.getRestaurantId()));
        }
    }

    // ===== Delete helper =====
    public static MenuItem buildWithId(Long id) {
        if (id == null) return null;
        MenuItem e = new MenuItem();
        e.setItemId(id);
        return e;
    }
}