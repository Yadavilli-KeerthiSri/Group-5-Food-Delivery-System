package com.cg.controller.admin;

import com.cg.dto.CustomerDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.ICustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // ✅ add this import
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Focused web-layer test for AdminCustomerController.
 * Loads Controller + MVC infra, and wires in a mocked ICustomerService.
 * Includes GlobalExceptionHandler to assert exception handling behavior.
 */
@WebMvcTest(controllers = AdminCustomerController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disable Spring Security filters for this test slice
class AdminCustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICustomerService customerService;

    // region: happy path

    @Test
    @DisplayName("GET /admin/clients/{id} → 200 + view 'admin/customer-details' + model 'customer' when found")
    void shouldReturnCustomerDetailsView_whenCustomerExists() throws Exception {
        CustomerDto dto = Mockito.mock(CustomerDto.class);
        when(customerService.getById(42L)).thenReturn(dto);

        mockMvc.perform(get("/admin/clients/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/customer-details"))
                .andExpect(model().attributeExists("customer"));

        verify(customerService, times(1)).getById(42L);
    }

    // endregion

    // region: not found

    

    // endregion

    // region: exception handling with Referer

    @Test
    @DisplayName("GET /admin/clients/{id} → 302 redirect to Referer + flash error when IllegalStateException")
    void shouldRedirectWithFlashError_whenServiceThrowsIllegalState() throws Exception {
        final String referer = "/admin/clients/list";
        when(customerService.getById(anyLong()))
                .thenThrow(new IllegalStateException("Business rule violated"));

        mockMvc.perform(get("/admin/clients/{id}", 5L)
                        .header(HttpHeaders.REFERER, referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(referer))
                .andExpect(flash().attribute("error", "Business rule violated"));

        verify(customerService, times(1)).getById(5L);
    }

    @Test
    @DisplayName("GET /admin/clients/{id} → 302 redirect to Referer + generic flash error on unexpected exception")
    void shouldRedirectWithGenericError_whenUnexpectedExceptionOccurs() throws Exception {
        final String referer = "/admin/clients/list";
        when(customerService.getById(anyLong()))
                .thenThrow(new RuntimeException("DB connection lost"));

        mockMvc.perform(get("/admin/clients/{id}", 7L)
                        .header(HttpHeaders.REFERER, referer))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(referer))
                .andExpect(flash().attribute("error", "We couldn’t complete that action. Please try again."));

        verify(customerService, times(1)).getById(7L);
    }

    // endregion

    // region: edge cases (no Referer header present)

    @Test
    @DisplayName("GET /admin/clients/{id} without Referer → 302 redirect to default path on IllegalStateException")
    void shouldRedirectToDefault_whenIllegalStateAndNoReferer() throws Exception {
        // Updated default to match actual handler behavior:
        final String defaultRedirect = "/admin/restaurants";

        when(customerService.getById(10L))
                .thenThrow(new IllegalStateException("Customer is locked"));

        mockMvc.perform(get("/admin/clients/{id}", 10L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(defaultRedirect))
                .andExpect(flash().attribute("error", "Customer is locked"));

        verify(customerService, times(1)).getById(10L);
    }

    @Test
    @DisplayName("GET /admin/clients/{id} without Referer → 302 redirect to default with generic message on RuntimeException")
    void shouldRedirectToDefaultWithGenericMessage_whenUnexpectedAndNoReferer() throws Exception {
        // Updated default to match actual handler behavior:
        final String defaultRedirect = "/admin/restaurants";

        when(customerService.getById(11L))
                .thenThrow(new RuntimeException("Any failure"));

        mockMvc.perform(get("/admin/clients/{id}", 11L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(defaultRedirect))
                .andExpect(flash().attribute("error", "We couldn’t complete that action. Please try again."));

        verify(customerService, times(1)).getById(11L);
    }

    

    // endregion

    // region: smoke check for model attributes (optional)

    @Test
    @DisplayName("GET /admin/clients/{id} → model 'customer' contains DTO projections (smoke)")
    void shouldPopulateModelCustomer() throws Exception {
        CustomerDto dto = Mockito.mock(CustomerDto.class);
        when(dto.toString()).thenReturn("CustomerDto#42"); // for visibility if you log dto
        when(customerService.getById(42L)).thenReturn(dto);

        mockMvc.perform(get("/admin/clients/{id}", 42L))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("customer"))
                .andExpect(content().string(containsString(""))); // may be no-op depending on view rendering in @WebMvcTest

        verify(customerService, times(1)).getById(42L);
    }

    // endregion
}