package com.cg.controller.admin;

import com.cg.dto.CustomerDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.ICustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Loads only the web layer for AdminCustomerController
@WebMvcTest(controllers = AdminCustomerController.class)
// Explicitly include your @ControllerAdvice
@Import(GlobalExceptionHandler.class)
class AdminCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    @Test
    @DisplayName("GET /admin/clients/{id} -> returns 'admin/customer-details' with model when customer exists")
    void shouldReturnCustomerDetailsView_whenCustomerExists() throws Exception {
        // Use a Mockito mock for DTO to avoid constructor/fields coupling
        CustomerDto dto = Mockito.mock(CustomerDto.class);
        when(customerService.getById(42L)).thenReturn(dto);

        mockMvc.perform(get("/admin/clients/{id}", 42L))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/customer-details"))
               .andExpect(model().attributeExists("customer"));
    }

    @Test
    @DisplayName("GET /admin/clients/{id} -> returns 'admin/customerNotFoundPage' when customer not found")
    void shouldReturnNotFoundView_whenCustomerDoesNotExist() throws Exception {
        when(customerService.getById(99L)).thenReturn(null);

        mockMvc.perform(get("/admin/clients/{id}", 99L))
               .andExpect(status().isOk())
               .andExpect(view().name("admin/customerNotFoundPage"));
    }

    @Test
    @DisplayName("GET /admin/clients/{id} -> redirects with flash error when service throws IllegalStateException")
    void shouldRedirectWithFlashError_whenServiceThrowsIllegalState() throws Exception {
        String referer = "/admin/clients/list";
        when(customerService.getById(anyLong()))
                .thenThrow(new IllegalStateException("Business rule violated"));

        mockMvc.perform(get("/admin/clients/{id}", 5L)
                        .header("Referer", referer))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(referer))
               .andExpect(flash().attribute("error", "Business rule violated"));
    }

    @Test
    @DisplayName("GET /admin/clients/{id} -> redirects with generic error when unexpected exception occurs")
    void shouldRedirectWithGenericError_whenUnexpectedExceptionOccurs() throws Exception {
        String referer = "/admin/clients/list";
        when(customerService.getById(anyLong()))
                .thenThrow(new RuntimeException("DB connection lost"));

        mockMvc.perform(get("/admin/clients/{id}", 7L)
                        .header("Referer", referer))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(referer))
               .andExpect(flash().attribute("error", "We couldn’t complete that action. Please try again."));
    }
}
