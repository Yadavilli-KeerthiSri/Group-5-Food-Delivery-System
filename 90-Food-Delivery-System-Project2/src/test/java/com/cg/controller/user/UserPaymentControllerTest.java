package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import com.cg.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserPaymentController: - Controller-only: all services
 * mocked. - Security filters disabled to avoid 401/403 in slice tests. -
 * Minimal view/redirect assertions and verify() calls.
 */
@WebMvcTest(UserPaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserPaymentControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PaymentService paymentService;

	@MockBean
	private OrderService orderService;

	@MockBean
	private CustomerService customerService;

	/* 1) PROFILE (GET /user/payment) -> returns profile-edit with 'user' */
	@Test
	@DisplayName("GET /user/payment → returns profile-edit with 'user'")
	void profile_shouldReturnProfileEditView() throws Exception {
		var auth = new TestingAuthenticationToken("user@example.com", "pwd");
		when(customerService.getByEmail("user@example.com")).thenReturn(new CustomerDto());

		mvc.perform(get("/user/payment").principal(auth)).andExpect(status().isOk())
				.andExpect(view().name("user/profile-edit")).andExpect(model().attributeExists("user"));

		verify(customerService).getByEmail("user@example.com");
	}

	/* 2) UPDATE (POST /user/payment/update) -> redirects and calls register() */
	@Test
	@DisplayName("POST /user/payment/update → redirects to /user/profile and calls register()")
	void update_shouldRedirectAndCallRegister() throws Exception {
		mvc.perform(post("/user/payment/update")
				// minimal form param to bind a CustomerDto (adjust if your controller needs
				// more)
				.param("customerId", "1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/profile"));

		verify(customerService).register(any(CustomerDto.class));
	}

	/*
	 * 3) CHECKOUT (POST /user/payment/checkout) -> returns 'user/payment' with
	 * 'order' model
	 */
	@Test
	@DisplayName("POST /user/payment/checkout → returns payment view with 'order' model")
	void checkout_shouldReturnPaymentViewWithOrderModel() throws Exception {
		// If your controller expects to create/fill a PaymentDto or similar, we only
		// assert model presence.
		mvc.perform(post("/user/payment/checkout").param("totalAmount", "499.99")).andExpect(status().isOk())
				.andExpect(view().name("user/payment")).andExpect(model().attributeExists("order")); // adjust key if
																										// your
																										// controller
																										// uses a
																										// different
																										// attribute
																										// name
	}

	/* 4) PROFILE (different user) -> same behavior */
	@Test
	@DisplayName("GET /user/payment (different user) → returns profile-edit with 'user'")
	void profile_withDifferentUser_shouldReturnView() throws Exception {
		var auth = new TestingAuthenticationToken("another@example.com", "pwd");
		when(customerService.getByEmail("another@example.com")).thenReturn(new CustomerDto());

		mvc.perform(get("/user/payment").principal(auth)).andExpect(status().isOk())
				.andExpect(view().name("user/profile-edit")).andExpect(model().attributeExists("user"));

		verify(customerService).getByEmail("another@example.com");
	}
}