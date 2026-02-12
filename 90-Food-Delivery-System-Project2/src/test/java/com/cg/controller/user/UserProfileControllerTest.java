package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.iservice.ICustomerService;
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
 * Very simple tests for UserProfileController: - Only checks view names,
 * redirects, and minimal verify() calls. - Security filters disabled to avoid
 * 401/403 in slice tests.
 */
@WebMvcTest(UserProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserProfileControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ICustomerService customerService;

	/* 1) VIEW PROFILE -> returns 'user/profile' with 'user' */
	@Test
	@DisplayName("GET /user/profile → returns profile view with 'user'")
	void profile_shouldReturnProfileView() throws Exception {
		var auth = new TestingAuthenticationToken("user@example.com", "pwd");
		when(customerService.getByEmail("user@example.com")).thenReturn(new CustomerDto());

		mvc.perform(get("/user/profile").principal(auth)).andExpect(status().isOk())
				.andExpect(view().name("user/profile")).andExpect(model().attributeExists("user"));

		verify(customerService).getByEmail("user@example.com");
	}

	/* 2) EDIT PROFILE -> returns 'user/profile-edit' with 'user' */
	@Test
	@DisplayName("GET /user/profile/edit → returns profile-edit view with 'user'")
	void edit_shouldReturnProfileEditView() throws Exception {
		var auth = new TestingAuthenticationToken("user@example.com", "pwd");
		when(customerService.getByEmail("user@example.com")).thenReturn(new CustomerDto());

		mvc.perform(get("/user/profile/edit").principal(auth)).andExpect(status().isOk())
				.andExpect(view().name("user/profile-edit")).andExpect(model().attributeExists("user"));

		verify(customerService).getByEmail("user@example.com");
	}

	/* 3) UPDATE -> redirects and calls register() */
	@Test
	@DisplayName("POST /user/profile/update → redirects to /user/profile and calls register()")
	void update_shouldRedirectAndCallRegister() throws Exception {
		var auth = new TestingAuthenticationToken("user@example.com", "pwd");
		CustomerDto current = new CustomerDto();
		current.setCustomerId(1L);
		current.setEmail("user@example.com");
		current.setRole("USER");
		when(customerService.getByEmail("user@example.com")).thenReturn(current);

		mvc.perform(post("/user/profile/update")
				// minimal form fields; controller overrides id/email/role anyway
				.param("firstName", "Mahesh").principal(auth)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/user/profile"));

		verify(customerService).getByEmail("user@example.com");
		verify(customerService).register(any(CustomerDto.class));
	}

	/* 4) UPDATE with different principal -> still redirects */
	@Test
	@DisplayName("POST /user/profile/update (different principal) → redirects to /user/profile and calls register()")
	void update_withDifferentUser_shouldRedirect() throws Exception {
		var auth = new TestingAuthenticationToken("another@example.com", "pwd");
		CustomerDto current = new CustomerDto();
		current.setCustomerId(2L);
		current.setEmail("another@example.com");
		current.setRole("USER");
		when(customerService.getByEmail("another@example.com")).thenReturn(current);

		mvc.perform(post("/user/profile/update").param("lastName", "Daggu").principal(auth))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/user/profile"));

		verify(customerService).getByEmail("another@example.com");
		verify(customerService).register(any(CustomerDto.class));
	}
}