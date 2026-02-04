package com.cg.model;

import com.cg.entity.MenuItem;

public class CartItem {

    private MenuItem item;
    private int quantity;

    public CartItem(MenuItem item) {
        this.item = item;
        this.quantity = 1;
    }

    // getters & setters
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }
    
	public MenuItem getItem() {
		return item;
	}
	public void setItem(MenuItem item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}   
}

