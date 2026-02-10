package com.cg.controller.user;

import com.cg.dto.OrderDto;
import com.cg.iservice.ICustomerService;
import com.cg.iservice.IMenuItemService;
import com.cg.iservice.IOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Very simple tests for UserOrderController:
 * - Only view/redirect checks and minimal verify().
 * - Avoids Authentication-heavy flows to keep tests lightweight.
 */
@WebMvcTest(UserOrderController.class)
class UserOrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IOrderService orderService;

    @MockBean
    private ICustomerService customerService;

    @MockBean
    private IMenuItemService menuItemService;

    /* 1) SUCCESS PAGE -> returns view */
    @Test
    void success_shouldReturnPaymentSuccessView() throws Exception {
        mvc.perform(get("/user/orders/success"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/payment-success"));
    }

    /* 2) ORDER DETAILS -> returns view with 'order' in model */
    @Test
    void orderDetails_shouldReturnDetailsView() throws Exception {
        when(orderService.getById(5L)).thenReturn(new OrderDto());

        mvc.perform(get("/user/orders/5"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/order-details"))
           .andExpect(model().attributeExists("order"));

        verify(orderService).getById(5L);
    }

    /* 3) CANCEL ORDER -> redirects to /user/orders */
    @Test
    void cancel_shouldRedirectToOrders() throws Exception {
        mvc.perform(get("/user/orders/cancel/8"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/user/orders"));

        verify(orderService).cancel(8L);
    }

    /* 4) VIEW MY ORDERS (by email) -> returns view with 'orders' */
    @Test
    void viewMyOrders_shouldReturnMyOrdersView() throws Exception {
        when(orderService.getOrdersByCustomerEmail("user@example.com"))
                .thenReturn(Collections.emptyList());

        mvc.perform(get("/user/orders/my-orders")
                        .principal((Principal) () -> "user@example.com"))
           .andExpect(status().isOk())
           .andExpect(view().name("user/my-orders"))
           .andExpect(model().attributeExists("orders"));

        verify(orderService).getOrdersByCustomerEmail("user@example.com");
    }
}
