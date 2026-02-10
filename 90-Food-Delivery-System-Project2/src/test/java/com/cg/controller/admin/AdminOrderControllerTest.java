package com.cg.controller.admin;

import com.cg.controller.admin.AdminOrderController;
import com.cg.dto.OrderDto;
import com.cg.service.CustomerService;
import com.cg.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminOrderController.class)
class AdminOrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CustomerService customerService;

    /* 1) LIST -> returns view and adds model attribute */
    @Test
    void list_shouldReturnOrderView() throws Exception {
        mvc.perform(get("/admin/orders"))
           .andExpect(status().isOk())
           .andExpect(view().name("admin/order"))
           .andExpect(model().attributeExists("orders"));

        verify(orderService).getAll();
    }

    /* 2) VIEW DETAILS -> returns view (controller currently only returns view) */
    @Test
    void viewDetails_shouldReturnOrderView() throws Exception {
        mvc.perform(get("/admin/orders/view/5"))
           .andExpect(status().isOk())
           .andExpect(view().name("admin/order"));
        // No service verification since method body currently returns view only.
    }

    /* 3) UPDATE STATUS (success) -> redirects with success flash */
    @Test
    void updateStatus_success_shouldRedirectWithSuccess() throws Exception {
        mvc.perform(post("/admin/orders/update/7"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/admin/orders"))
           .andExpect(flash().attributeExists("success"));

        verify(orderService).updateStatus(7L);
    }

    /* 4) UPDATE STATUS (failure) -> redirects with error flash */
    @Test
    void updateStatus_failure_shouldRedirectWithError() throws Exception {
        doThrow(new RuntimeException("boom")).when(orderService).updateStatus(9L);

        mvc.perform(post("/admin/orders/update/9"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrl("/admin/orders"))
           .andExpect(flash().attributeExists("error"));

        verify(orderService).updateStatus(9L);
    }
}