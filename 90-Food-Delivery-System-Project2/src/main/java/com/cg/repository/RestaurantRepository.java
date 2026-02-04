package com.cg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	List<Restaurant> findByRestaurantNameContainingIgnoreCase(String keyword);

    List<Restaurant> findByCuisineContainingIgnoreCase(String cuisine);

    List<Restaurant> findByLocationContainingIgnoreCase(String location);
}
