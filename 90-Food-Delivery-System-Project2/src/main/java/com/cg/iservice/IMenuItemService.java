package com.cg.iservice;

import java.util.List;

import com.cg.dto.MenuItemDto;

public interface IMenuItemService {

    MenuItemDto add(MenuItemDto dto);

    MenuItemDto update(MenuItemDto dto);

    MenuItemDto getById(Long id);

    List<MenuItemDto> getByRestaurant(Long restaurantId);

    List<MenuItemDto> getAll();

    void delete(Long id);
}