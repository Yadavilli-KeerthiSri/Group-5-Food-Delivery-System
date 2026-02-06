package com.cg.mapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.cg.dto.RestaurantDto;
import com.cg.entity.MenuItem;
import com.cg.entity.Restaurant;

public final class RestaurantMapper {

    private RestaurantMapper() {}

    // ===== Read =====
    public static RestaurantDto toDto(Restaurant entity) {
        if (entity == null) return null;
        RestaurantDto dto = new RestaurantDto();
        dto.setRestaurantId(entity.getRestaurantId());
        dto.setRestaurantName(entity.getRestaurantName());
        dto.setLocation(entity.getLocation());
        dto.setCuisine(entity.getCuisine());
        dto.setRatings(entity.getRatings());

        List<Long> itemIds = entity.getMenuItems() == null ? null :
            entity.getMenuItems().stream()
                  .filter(Objects::nonNull)
                  .map(MenuItem::getItemId)
                  .collect(Collectors.toList());
        dto.setMenuItemIds(itemIds);
        return dto;
    }

    // ===== Create =====
    public static Restaurant fromCreateDto(
            RestaurantDto dto,
            Function<List<Long>, List<MenuItem>> menuItemsResolver
    ) {
        if (dto == null) return null;
        Restaurant entity = new Restaurant();
        entity.setRestaurantId(dto.getRestaurantId());
        entity.setRestaurantName(dto.getRestaurantName());
        entity.setLocation(dto.getLocation());
        entity.setCuisine(dto.getCuisine());
        entity.setRatings(dto.getRatings());

        if (dto.getMenuItemIds() != null && menuItemsResolver != null) {
            entity.setMenuItems(menuItemsResolver.apply(dto.getMenuItemIds()));
        }
        return entity;
    }

    // ===== Update =====
    public static void applyUpdate(
            RestaurantDto dto,
            Restaurant target,
            Function<List<Long>, List<MenuItem>> menuItemsResolver
    ) {
        if (dto == null || target == null) return;
        target.setRestaurantName(dto.getRestaurantName());
        target.setLocation(dto.getLocation());
        target.setCuisine(dto.getCuisine());
        target.setRatings(dto.getRatings());

        if (dto.getMenuItemIds() != null && menuItemsResolver != null) {
            target.setMenuItems(menuItemsResolver.apply(dto.getMenuItemIds()));
        }
    }

    // ===== Delete helper =====
    public static Restaurant buildWithId(Long id) {
        if (id == null) return null;
        Restaurant e = new Restaurant();
        e.setRestaurantId(id);
        return e;
    }
}