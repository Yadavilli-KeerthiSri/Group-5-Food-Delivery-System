package com.cg.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.MenuItemDto;
import com.cg.entity.MenuItem;
import com.cg.entity.Order;
import com.cg.mapper.MenuItemMapper;
import com.cg.repository.MenuItemRepository;
import com.cg.repository.OrderRepository;
import com.cg.repository.RestaurantRepository;
import com.cg.iservice.IMenuItemService;

@Service
public class MenuItemService implements IMenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public MenuItemDto add(MenuItemDto dto) {
        MenuItem entity = MenuItemMapper.fromCreateDto(
                dto,
                restId -> restaurantRepository.findById(restId).orElse(null)
        );
        MenuItem saved = menuItemRepository.save(entity);
        return MenuItemMapper.toDto(saved);
    }

    @Override
    public MenuItemDto update(MenuItemDto dto) {
        MenuItem existing = menuItemRepository.findById(dto.getItemId()).orElseThrow();
        MenuItemMapper.applyUpdate(
                dto,
                existing,
                restId -> restaurantRepository.findById(restId).orElse(null)
        );
        MenuItem saved = menuItemRepository.save(existing);
        return MenuItemMapper.toDto(saved);
    }

    @Override
    public MenuItemDto getById(Long id) {
        return menuItemRepository.findById(id)
                .map(MenuItemMapper::toDto)
                .orElseThrow();
    }

    @Override
    public List<MenuItemDto> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantRestaurantId(restaurantId)
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<MenuItem> itemOptional = menuItemRepository.findById(id);
        if (itemOptional.isPresent()) {
            MenuItem item = itemOptional.get();

            // Clear relationships in the Order table (as per your original logic)
            if (item.getOrders() != null) {
                for (Order order : item.getOrders()) {
                    order.setItems(null);
                    orderRepository.save(order);
                }
            }

            menuItemRepository.delete(item);
            System.out.println("Item " + id + " deleted successfully.");
        } else {
            System.out.println("Item " + id + " was already deleted or does not exist.");
        }
    }

    @Override
    public List<MenuItemDto> getAll() {
        return menuItemRepository.findAll()
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }
}