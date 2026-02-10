package com.cg.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Super-simple tests for CartService:
 * - add (accumulates quantity)
 * - remove
 * - getCart (contains expected entries)
 * - clear
 */
class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService();
    }

    /* 1) addItemToCart: inserts and accumulates quantity */
    @Test
    void addItemToCart_shouldAccumulateQuantity() {
        cartService.addItemToCart(1L, 2);
        cartService.addItemToCart(1L, 3);

        Map<Long, Integer> cart = cartService.getCart();
        assertEquals(1, cart.size());
        assertEquals(5, cart.get(1L));
    }

    /* 2) removeItemFromCart: removes item if present */
    @Test
    void removeItemFromCart_shouldRemove() {
        cartService.addItemToCart(2L, 1);
        cartService.removeItemFromCart(2L);

        assertFalse(cartService.getCart().containsKey(2L));
    }

    /* 3) getCart: reflects current content */
    @Test
    void getCart_shouldReturnCurrentState() {
        cartService.addItemToCart(10L, 4);

        Map<Long, Integer> cart = cartService.getCart();
        assertEquals(1, cart.size());
        assertEquals(4, cart.get(10L));
    }

    /* 4) clearCart: empties everything */
    @Test
    void clearCart_shouldEmptyCart() {
        cartService.addItemToCart(3L, 2);
        cartService.addItemToCart(4L, 5);

        cartService.clearCart();

        assertTrue(cartService.getCart().isEmpty());
    }
}
