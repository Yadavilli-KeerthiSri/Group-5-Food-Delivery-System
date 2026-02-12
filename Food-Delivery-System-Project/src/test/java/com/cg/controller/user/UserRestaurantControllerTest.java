package com.cg.controller.user;

import com.cg.dto.RestaurantDto;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserRestaurantController: - Controller-only: service
 * layer mocked. - Security filters disabled to avoid 401/403. - Checks view
 * names + model attributes + minimal verifies.
 */
@WebMvcTest(UserRestaurantController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserRestaurantControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private IRestaurantService restaurantService;

	@MockBean
	private IMenuItemService menuItemService;

	/* 1) LIST (default filter=all) -> returns 'user/restaurants' */
	@Test
	@DisplayName("GET /user/restaurants → list all restaurants")
	void list_default_shouldReturnRestaurantsView() throws Exception {
		when(restaurantService.getAll()).thenReturn(Collections.emptyList());

		mvc.perform(get("/user/restaurants")).andExpect(status().isOk()).andExpect(view().name("user/restaurants"))
				.andExpect(model().attributeExists("restaurants"));

		verify(restaurantService).getAll();
	}

	/* 2) LIST (filter=top) -> calls findTopRated() */
	@Test
	@DisplayName("GET /user/restaurants?filter=top → top-rated restaurants")
	void list_top_shouldReturnRestaurantsView() throws Exception {
		when(restaurantService.findTopRated()).thenReturn(Collections.emptyList());

		mvc.perform(get("/user/restaurants").param("filter", "top")).andExpect(status().isOk())
				.andExpect(view().name("user/restaurants")).andExpect(model().attributeExists("restaurants"));

		verify(restaurantService).findTopRated();
	}

	/* 3) VIEW MENU */
	@Test
	@DisplayName("GET /user/restaurants/{id} → restaurant menu page")
	void viewMenu_shouldReturnRestaurantMenuView() throws Exception {
		when(restaurantService.getById(7L)).thenReturn(new RestaurantDto());
		when(menuItemService.getByRestaurant(7L)).thenReturn(Collections.emptyList());

		mvc.perform(get("/user/restaurants/7")).andExpect(status().isOk())
				.andExpect(view().name("user/restaurant-menu")).andExpect(model().attributeExists("restaurant"))
				.andExpect(model().attributeExists("menuItems"));

		verify(restaurantService).getById(7L);
		verify(menuItemService).getByRestaurant(7L);
	}

	/* 4) DASHBOARD */
	@Test
	@DisplayName("GET /user/restaurants/dashboard → dashboard view")
	void dashboard_shouldReturnUserDashboard() throws Exception {
		when(restaurantService.findTopForDashboard()).thenReturn(Collections.emptyList());

		mvc.perform(get("/user/restaurants/dashboard")).andExpect(status().isOk())
				.andExpect(view().name("user/user-dashboard")).andExpect(model().attributeExists("restaurants"));

		verify(restaurantService).findTopForDashboard();
	}
}