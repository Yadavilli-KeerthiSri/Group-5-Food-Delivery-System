package com.cg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CartService {
	// Store: itemId -> quantity mapping
	private Map<Long, Integer> cart = new HashMap<>();

	public void addItemToCart(Long itemId, int quantity) {
	cart.put(itemId, cart.getOrDefault(itemId, 0) + quantity);
	}

	public void removeItemFromCart(Long itemId) {
	cart.remove(itemId);
	}

	public Map<Long, Integer> getCart() {
	return cart;
	}
	
	public void clearCart() {
	cart.clear();
	}
}
