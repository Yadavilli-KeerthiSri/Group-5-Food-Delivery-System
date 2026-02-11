package com.cg.controller.admin;

import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminMenuItemController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false) // disable Spring Security filters for this slice
class AdminMenuItemControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IMenuItemService menuItemService;

	@MockBean
	private IRestaurantService restaurantService;

	// 1️⃣ POSITIVE — List all menu items
	@Test
	@DisplayName("GET /admin/menu-items → shows items list page")
	void list_shouldReturnItemsView() throws Exception {
		when(menuItemService.getAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/admin/menu-items")).andExpect(status().isOk()).andExpect(view().name("admin/menu-items"))
				.andExpect(model().attributeExists("items"));
	}

	// 2️⃣ POSITIVE — Add form loads with empty item + restaurants
	@Test
	@DisplayName("GET /admin/menu-items/add → shows add form with restaurants")
	void addForm_shouldReturnFormWithRestaurants() throws Exception {
		when(restaurantService.getAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/admin/menu-items/add")).andExpect(status().isOk())
				.andExpect(view().name("admin/menu-item-form")).andExpect(model().attributeExists("item"))
				.andExpect(model().attributeExists("restaurants"));
	}

	// 3️⃣ POSITIVE — Save (create) redirects to list
	@Test
	@DisplayName("POST /admin/menu-items/save (create) → redirects to list")
	void save_create_shouldRedirectToList() throws Exception {
		// For void methods (add/update), no stubbing required unless you want to throw

		mockMvc.perform(post("/admin/menu-items/save")
				// Minimal params to bind; controller sets restaurantId explicitly
				.param("restaurantId", "1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/menu-items"));
	}

	// 4️⃣ EXCEPTION — Delete throws IllegalStateException → redirect to Referer
	// with flash error
	@Test
	@DisplayName("GET /admin/menu-items/delete/{id} throws IllegalStateException → redirect back with error")
	void delete_whenBusinessRuleViolation_shouldRedirectBackWithFlash() throws Exception {
		// delete is most likely void → use doThrow for void methods
		doThrow(new IllegalStateException("Cannot delete this item")).when(menuItemService).delete(anyLong());

		mockMvc.perform(get("/admin/menu-items/delete/5").header(HttpHeaders.REFERER, "/admin/menu-items"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/menu-items"))
				.andExpect(flash().attribute("error", "Cannot delete this item"));
	}

	// 🔁 If you instead want to cover unexpected error on list (optional
	// replacement for #3):
	// @Test
	// @DisplayName("Unexpected error → redirect to /admin/restaurants with generic
	// error")
	// void list_whenUnexpectedException_shouldRedirectToFallback() throws Exception
	// {
	// when(menuItemService.getAll()).thenThrow(new RuntimeException("DB down"));
	//
	// mockMvc.perform(get("/admin/menu-items"))
	// .andExpect(status().is3xxRedirection())
	// .andExpect(redirectedUrl("/admin/restaurants"))
	// .andExpect(flash().attribute("error",
	// "We couldn’t complete that action. Please try again."));
	// }
}