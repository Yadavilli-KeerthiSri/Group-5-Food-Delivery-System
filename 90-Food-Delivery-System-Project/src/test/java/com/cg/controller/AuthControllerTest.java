package com.cg.controller;

import com.cg.dto.CustomerDto;
import com.cg.service.CustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Focused web-layer test for AuthController. Security filters disabled to match
 * the reference pattern and avoid CSRF issues.
 */
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	// Must match the concrete type autowired in the controller
	@MockBean
	private CustomerService customerService;

	// Controller autowires this; provide a mock so the slice can start
	@MockBean
	private PasswordEncoder passwordEncoder;

	@Test
	@DisplayName("GET /login → 200 + view 'auth/login'")
	void getLogin_returnsLoginView() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("auth/login"));
	}

	@Test
	@DisplayName("GET /register → 200 + view 'auth/register'")
	void getRegister_returnsRegisterView() throws Exception {
		mockMvc.perform(get("/register")).andExpect(status().isOk()).andExpect(view().name("auth/register"));
	}

	@Test
	@DisplayName("POST /register → 302 /login, service called once, role forced to ROLE_USER, password unchanged")
	void postRegister_redirects_callsService_setsRoleUser_andPassesPlainPassword() throws Exception {
		// Arrange
		final String plainPassword = "plain123";
		final String incomingRole = "ROLE_ADMIN"; // should be ignored

		// Act
		mockMvc.perform(post("/register")
				// 🔁 Make sure these match your CustomerDto fields
				.param("firstName", "John").param("lastName", "Doe").param("email", "john.doe@example.com")
				.param("password", plainPassword).param("role", incomingRole)).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));

		// Assert: captured DTO
		ArgumentCaptor<CustomerDto> captor = ArgumentCaptor.forClass(CustomerDto.class);
		verify(customerService, times(1)).saveUser(captor.capture());
		CustomerDto saved = captor.getValue();

		assertThat(saved).as("DTO passed to service must not be null").isNotNull();
		assertThat(saved.getRole()).isEqualTo("ROLE_USER"); // overridden
		assertThat(saved.getPassword()).isEqualTo(plainPassword); // unchanged here
	}

	@Test
	@DisplayName("POST /register ignores posted role and always sets ROLE_USER")
	void postRegister_ignoresPostedRole() throws Exception {
		mockMvc.perform(
				post("/register").param("email", "a@b.com").param("password", "pwd").param("role", "ROLE_SUPER_ADMIN"))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));

		ArgumentCaptor<CustomerDto> captor = ArgumentCaptor.forClass(CustomerDto.class);
		verify(customerService, times(1)).saveUser(captor.capture());
		assertThat(captor.getValue().getRole()).isEqualTo("ROLE_USER");
	}
}