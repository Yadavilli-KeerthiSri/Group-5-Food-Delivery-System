package com.cg.controller.admin;

import com.cg.exception.GlobalExceptionHandler;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOrderController.class)
@AutoConfigureMockMvc(addFilters = false) // disable Spring Security filters in this test slice
@Import(GlobalExceptionHandler.class) // include only if you want unexpected errors to redirect
class AdminOrderControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private OrderService orderService;

	@MockBean
	private CustomerService customerService;

	// 1) LIST — returns view and adds model attribute
	@Test
	@DisplayName("GET /admin/orders → shows orders page")
	void list_shouldReturnOrderView() throws Exception {
		when(orderService.getAll()).thenReturn(Collections.emptyList());

		mvc.perform(get("/admin/orders")).andExpect(status().isOk()).andExpect(view().name("admin/order"))
				.andExpect(model().attributeExists("orders"));

		verify(orderService).getAll();
	}

	// 2) VIEW DETAILS — returns view (controller currently just returns view)
	@Test
	@DisplayName("GET /admin/orders/view/{id} → returns order view")
	void viewDetails_shouldReturnOrderView() throws Exception {
		mvc.perform(get("/admin/orders/view/5")).andExpect(status().isOk()).andExpect(view().name("admin/order"));

		// No service verify if controller doesn't call service for this action.
	}

	// 3) UPDATE STATUS (success) — redirects with success flash
	@Test
	@DisplayName("PUT /admin/orders/update/{id} (success) → redirects with success")
	void updateStatus_success_shouldRedirectWithSuccess() throws Exception {
		// If updateStatus is void, no stubbing needed for success path.

		mvc.perform(put("/admin/orders/update/7")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/orders")).andExpect(flash().attributeExists("success"));

		verify(orderService).updateStatus(7L);
	}

	// 4) UPDATE STATUS (failure) — redirects with error flash (use doThrow for
	// void)
	@Test
	@DisplayName("PUT /admin/orders/update/{id} (failure) → redirects with error")
	void updateStatus_failure_shouldRedirectWithError() throws Exception {
		doThrow(new RuntimeException("boom")).when(orderService).updateStatus(9L);

		mvc.perform(put("/admin/orders/update/9")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/admin/orders")).andExpect(flash().attributeExists("error"));

		verify(orderService).updateStatus(9L);
	}
}