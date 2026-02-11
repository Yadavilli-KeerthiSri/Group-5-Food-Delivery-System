package com.cg.controller.admin;

import com.cg.dto.RestaurantDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IRestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminRestaurantController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false) // 🔒 disable Spring Security filters for this test slice
class AdminRestaurantControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private IRestaurantService restaurantService;

	/* 1) CREATE */
	@Test
	@DisplayName("POST /admin/restaurants/save (create) → redirects to list and calls add()")
	void save_create_shouldRedirect() throws Exception {
		// No stubbing needed for success path if add() is void

		mvc.perform(post("/admin/restaurants/save")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/restaurants"));

		verify(restaurantService).add(any(RestaurantDto.class));
	}

	/* 2) UPDATE */
	@Test
	@DisplayName("POST /admin/restaurants/save (update) with restaurantId → redirects to list and calls update()")
	void save_update_shouldRedirect() throws Exception {
		mvc.perform(post("/admin/restaurants/save").param("restaurantId", "10")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/restaurants"));

		verify(restaurantService).update(any(RestaurantDto.class));
	}

	/* 3) DELETE -> FK ERROR (handled by GlobalExceptionHandler) */
	@Test
	@DisplayName("GET /admin/restaurants/delete/{id} with FK violation → redirects back with flash error")
	void delete_fkError_shouldRedirectWithError() throws Exception {
		// delete(...) is a void method → use doThrow
		doThrow(new DataIntegrityViolationException("FK")).when(restaurantService).delete(5L);

		mvc.perform(get("/admin/restaurants/delete/5").header(HttpHeaders.REFERER, "/admin/restaurants"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/restaurants"))
				.andExpect(flash().attributeExists("error"));
	}

	/* 4) EDIT -> ILLEGAL STATE (handled by GlobalExceptionHandler) */
	@Test
	@DisplayName("GET /admin/restaurants/edit/{id} throws IllegalStateException → redirects back with flash error")
	void edit_illegalState_shouldRedirectWithError() throws Exception {
		when(restaurantService.getById(15L)).thenThrow(new IllegalStateException("not allowed"));

		mvc.perform(get("/admin/restaurants/edit/15").header(HttpHeaders.REFERER, "/admin/restaurants"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/admin/restaurants"))
				.andExpect(flash().attributeExists("error"));
	}
}