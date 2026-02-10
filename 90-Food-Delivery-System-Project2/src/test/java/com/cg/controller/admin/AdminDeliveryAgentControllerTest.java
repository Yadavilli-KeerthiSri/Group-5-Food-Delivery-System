package com.cg.controller.admin;

import com.cg.dto.DeliveryAgentDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IDeliveryAgentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminDeliveryAgentController.class)
@Import(GlobalExceptionHandler.class)
class AdminDeliveryAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDeliveryAgentService deliveryAgentService;

    @Test
    @DisplayName("POST /admin/agents/save (create) -> calls add() and redirects to list")
    void save_whenCreate_shouldCallAddAndRedirect() throws Exception {
        // No agentId param -> create flow
        mockMvc.perform(post("/admin/agents/save")
                        .contentType("application/x-www-form-urlencoded"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/agents"));

        verify(deliveryAgentService, times(1)).add(any(DeliveryAgentDto.class));
        verify(deliveryAgentService, never()).update(any(DeliveryAgentDto.class));
    }

    @Test
    @DisplayName("POST /admin/agents/save (update) -> calls update() and redirects to list")
    void save_whenUpdate_shouldCallUpdateAndRedirect() throws Exception {
        // agentId present -> update flow
        mockMvc.perform(post("/admin/agents/save")
                        .contentType("application/x-www-form-urlencoded")
                        .param("agentId", "12"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/admin/agents"));

        verify(deliveryAgentService, times(1)).update(any(DeliveryAgentDto.class));
        verify(deliveryAgentService, never()).add(any(DeliveryAgentDto.class));
    }

    @Test
    @DisplayName("GET /admin/agents/edit/{id} -> on IllegalStateException redirects to Referer with error message")
    void editForm_whenIllegalState_shouldRedirectToRefererWithFlash() throws Exception {
        long id = 7L;
        String referer = "/admin/agents";
        String msg = "Agent is inactive and cannot be edited";

        when(deliveryAgentService.getById(eq(id)))
                .thenThrow(new IllegalStateException(msg));

        mockMvc.perform(get("/admin/agents/edit/{id}", id)
                        .header("Referer", referer))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(referer))
               .andExpect(flash().attribute("error", msg));
    }

    @Test
    @DisplayName("GET /admin/agents/delete/{id} -> on FK violation redirects to Referer with agent-specific message")
    void delete_whenDataIntegrityViolation_shouldRedirectToRefererWithAgentMessage() throws Exception {
        long id = 5L;
        String referer = "/admin/agents";
        String expectedMsg = "Cannot delete this delivery agent because there are deliveries or assignments linked.";

        doThrow(new DataIntegrityViolationException("FK violation"))
                .when(deliveryAgentService).delete(eq(id));

        mockMvc.perform(get("/admin/agents/delete/{id}", id)
                        .header("Referer", referer))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(referer))
               .andExpect(flash().attribute("error", expectedMsg));
    }
}
