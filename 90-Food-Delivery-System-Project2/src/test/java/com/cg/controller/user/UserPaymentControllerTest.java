package com.cg.controller.user;

import com.cg.dto.CustomerDto;
import com.cg.dto.PaymentDto;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import com.cg.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserPaymentController:
 * - Only basic view/redirect checks and minimal verify().
 * - Uses TestingAuthenticationToken for Authentication.
 */
@WebMvcTest(UserPaymentController.class)
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
    void profile_shouldReturnProfileEditView() throws Exception {
        var auth = new TestingAuthenticationToken("user@example.com", "pwd");
        when(customerService.getByEmail("user@example.com")).thenReturn(new CustomerDto());

        mvc.perform(get("/user/payment").principal(auth))
           .andExpect(status().isOk())
           .andExpect(view().name("user/profile-edit"))
           .andExpect(model().attributeExists("user"));

        verify(customerService).getByEmail("user@example.com");
    }

    /* 2) UPDATE (POST /user/payment/update) -> redirects and calls register() */
    @Test
    void update_shouldRedirectAndCallRegister() throws Exception {
        mvc.perform(post("/user/payment/update")
                        .param("customerId", "1")) // minimal form param
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/user/profile"));

        verify(customerService).register(any(CustomerDto.class));
    }

    /* 3) CHECKOUT (POST /user/payment/checkout) -> returns payment view with 'order' */
    @Test
    void checkout_shouldReturnPaymentViewWithOrderModel() throws Exception {
        mvc.perform(post("/user/payment/checkout")
                        .param("totalAmount", "499.99"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/payment"))
           .andExpect(model().attributeExists("order")); // PaymentDto added as 'order'
    }

    /* 4) PROFILE (no auth details beyond principal name) -> still returns view */
    @Test
    void profile_withDifferentUser_shouldReturnView() throws Exception {
        var auth = new TestingAuthenticationToken("another@example.com", "pwd");
        when(customerService.getByEmail("another@example.com")).thenReturn(new CustomerDto());

        mvc.perform(get("/user/payment").principal(auth))
           .andExpect(status().isOk())
           .andExpect(view().name("user/profile-edit"))
           .andExpect(model().attributeExists("user"));

        verify(customerService).getByEmail("another@example.com");
    }
}
