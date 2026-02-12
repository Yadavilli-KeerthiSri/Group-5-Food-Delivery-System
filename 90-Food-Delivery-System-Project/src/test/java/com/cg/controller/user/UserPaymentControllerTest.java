package com.cg.controller.user;

import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import com.cg.service.PaymentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

	/*
	 * 1) CHECKOUT (POST /user/payment/checkout) -> returns 'user/payment' with
	 * 'order' model
	 */
	@Test
	@DisplayName("POST /user/payment/checkout → returns payment view with 'order' model")
	void checkout_shouldReturnPaymentViewWithOrderModel() throws Exception {
		// If your controller expects to create/fill a PaymentDto or similar, we only
		// assert model presence.
		mvc.perform(post("/user/payment/checkout").param("totalAmount", "499.99")).andExpect(status().isOk())
				.andExpect(view().name("user/payment")).andExpect(model().attributeExists("order")); 
	}

}