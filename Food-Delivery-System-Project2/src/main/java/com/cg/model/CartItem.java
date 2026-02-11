package com.cg.model;

import com.cg.dto.MenuItemDto;

public class CartItem {

    private MenuItemDto item;
    private int quantity;

    public CartItem(MenuItemDto item) {
        this.item = item;
        this.quantity = 1;
    }

    // Subtotal calculation using DTO field
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }

    public MenuItemDto getItem() {
        return item;
    }

    public void setItem(MenuItemDto item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}