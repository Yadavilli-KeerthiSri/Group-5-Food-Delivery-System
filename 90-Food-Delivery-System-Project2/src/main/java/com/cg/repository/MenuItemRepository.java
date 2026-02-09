package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.MenuItem;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
	List<MenuItem> findByRestaurantRestaurantId(Long restaurantId);

    List<MenuItem> findByItemNameContainingIgnoreCase(String keyword);

    List<MenuItem> findTop10ByOrderByPriceAsc();
    
    MenuItem findByItemName(String itemName);
    
    boolean existsByRestaurant_RestaurantId(Long restaurantId); 
}
