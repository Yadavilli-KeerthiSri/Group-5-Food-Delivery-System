package com.cg.controller.user;

import com.cg.model.CartItem;
import com.cg.service.MenuItemService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user/cart")
@SessionAttributes("cart")
public class UserCartController {

	@Autowired
	private MenuItemService menuItemService;

	@ModelAttribute("cart")
	public Map<Long, CartItem> cart() {
		return new HashMap<>();
	}

	/* ADD ITEM */
	@GetMapping("/add/{id}")
	public String add(@PathVariable Long id, @ModelAttribute("cart") Map<Long, CartItem> cart,
			HttpServletRequest request) {
		cart.compute(id, (key, cartItem) -> {
			if (cartItem == null) {
				return new CartItem(menuItemService.getById(id));
			}
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			return cartItem;
		});
		// Get the previous page URL (restaurant menu page)
		String referer = request.getHeader("Referer");
		return "redirect:" + (referer != null ? referer : "/user/restaurants/dashboard");
	}

	/* INCREASE QTY */
	@GetMapping("/increase/{id}")
	public String increase(@PathVariable Long id, @ModelAttribute("cart") Map<Long, CartItem> cart) {

		cart.get(id).setQuantity(cart.get(id).getQuantity() + 1);
		return "redirect:/user/cart/view";
	}

	/* DECREASE QTY */
	@GetMapping("/decrease/{id}")
	public String decrease(@PathVariable Long id, @ModelAttribute("cart") Map<Long, CartItem> cart) {

		CartItem item = cart.get(id);
		if (item.getQuantity() == 1) {
			cart.remove(id);
		} else {
			item.setQuantity(item.getQuantity() - 1);
		}
		return "redirect:/user/cart/view";
	}

	/* REMOVE ITEM */
	@GetMapping("/remove/{id}")
	public String remove(@PathVariable Long id, @ModelAttribute("cart") Map<Long, CartItem> cart) {

		cart.remove(id);
		return "redirect:/user/cart/view";
	}

	/* VIEW CART */
	@GetMapping("/view")
	public String view(@ModelAttribute("cart") Map<Long, CartItem> cart, Model model) {

		// 1. Calculate Grand Total
		double total = cart.values().stream().mapToDouble(CartItem::getSubtotal).sum();

		// 2. Logic: Determine the "Back" link from the data, not the browser history
		String previousPage = "/user/dashboard"; // Default fallback

		if (cart != null && !cart.isEmpty()) {
			// Get the first item in the cart to find which restaurant we are at
			CartItem firstEntry = cart.values().iterator().next();
			if (firstEntry.getItem() != null && firstEntry.getItem().getRestaurantId() != null) {
				previousPage = "/user/restaurants/" + firstEntry.getItem().getRestaurantId();
			}
		}

		model.addAttribute("cartItems", cart.values());
		model.addAttribute("total", total);
		model.addAttribute("previousPage", previousPage);

		return "user/cart";
	}

	@GetMapping("/add-more")
	public String addMore(@ModelAttribute("cart") Map<Long, CartItem> cart) {
		if (cart == null || cart.isEmpty()) {
			// If cart is empty, go to restaurants list (or /user/dashboard if you prefer)
			return "redirect:/user/restaurants";
		}

		// Derive restaurantId from any item in the cart
		CartItem any = cart.values().iterator().next();
		Long restaurantId = (any != null && any.getItem() != null) ? any.getItem().getRestaurantId() : null;
		// CRITICAL: append ?fromCart=1 so the Menu page knows you arrived from Cart
		return (restaurantId != null) ? "redirect:/user/restaurants/" + restaurantId + "?fromCart=1"
				: "redirect:/user/restaurants";
	}
}
