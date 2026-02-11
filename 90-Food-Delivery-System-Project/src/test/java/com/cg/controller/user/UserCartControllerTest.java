package com.cg.controller.user;

import com.cg.model.CartItem;
import com.cg.service.MenuItemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserCartController. - Controller-only: MenuItemService
 * is mocked. - Uses session attribute "cart" as a simple HashMap<Long,
 * CartItem>. - Security filters disabled to avoid 401/403 in slice tests.
 */
@WebMvcTest(UserCartController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserCartControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MenuItemService menuItemService;

	/* 1) ADD -> redirects to Referer and calls service */
	@Test
	@DisplayName("GET /user/cart/add/{id} → redirects back and loads item")
	void add_shouldRedirectBackAndCallService() throws Exception {
		Map<Long, CartItem> cart = new HashMap<>();

		mvc.perform(get("/user/cart/add/11").header("Referer", "/user/restaurants/3").sessionAttr("cart", cart))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/restaurants/3"));

		verify(menuItemService).getById(11L);
	}

	/* 2) INCREASE -> redirects to /user/cart/view */
	@Test
	@DisplayName("GET /user/cart/increase/{id} → redirects to cart view and bumps quantity")
	void increase_shouldRedirectToView() throws Exception {
		Map<Long, CartItem> cart = new HashMap<>();
		CartItem item = mock(CartItem.class);
		when(item.getQuantity()).thenReturn(1); // minimal stub
		cart.put(5L, item);

		mvc.perform(get("/user/cart/increase/5").sessionAttr("cart", cart)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/cart/view"));

		// optional: verify quantity setter invoked at least once
		verify(item, atLeastOnce()).setQuantity(anyInt());
	}

	/*
	 * 3) DECREASE when qty == 1 -> redirects (controller usually removes item or
	 * keeps it at 1)
	 */
	@Test
	@DisplayName("GET /user/cart/decrease/{id} (qty=1) → redirects to cart view")
	void decrease_qtyOne_shouldRedirectToView() throws Exception {
		Map<Long, CartItem> cart = new HashMap<>();
		CartItem item = mock(CartItem.class);
		when(item.getQuantity()).thenReturn(1);
		cart.put(7L, item);

		mvc.perform(get("/user/cart/decrease/7").sessionAttr("cart", cart)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/cart/view"));
	}

	/* 4) VIEW (empty cart) -> returns view with default previousPage and total */
	@Test
	@DisplayName("GET /user/cart/view (empty cart) → returns user/cart with basic model")
	void view_emptyCart_shouldReturnView() throws Exception {
		Map<Long, CartItem> emptyCart = new HashMap<>();

		mvc.perform(get("/user/cart/view").sessionAttr("cart", emptyCart)).andExpect(status().isOk())
				.andExpect(view().name("user/cart")).andExpect(model().attributeExists("cartItems"))
				.andExpect(model().attributeExists("total")).andExpect(model().attributeExists("previousPage"));
	}
}