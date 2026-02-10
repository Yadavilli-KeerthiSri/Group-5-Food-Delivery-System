package com.cg.controller.admin;

import com.cg.dto.DeliveryAgentDto;
import com.cg.exception.GlobalExceptionHandler;
import com.cg.iservice.IDeliveryAgentService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminDeliveryAgentController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)   // Disable Spring Security
class AdminDeliveryAgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDeliveryAgentService service;

    // 1️⃣ POSITIVE — LIST ALL AGENTS
    @Test
    @DisplayName("GET /admin/agents → returns agents list page")
    void testListAgents() throws Exception {

        when(service.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/agents"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/agents"))
                .andExpect(model().attributeExists("agents"));
    }

    // 2️⃣ POSITIVE — EDIT FORM LOADS EVEN IF AGENT DOES NOT EXIST
    @Test
    @DisplayName("GET /admin/agents/edit/{id} → loads edit form")
    void testEditForm() throws Exception {

        // To avoid Thymeleaf crashing, return an empty DTO
        when(service.getById(10L)).thenReturn(new DeliveryAgentDto());

        mockMvc.perform(get("/admin/agents/edit/10"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/agent-form"))
                .andExpect(model().attributeExists("agent"));
    }

    // 3️⃣ EXCEPTION — BUSINESS RULE → IllegalStateException
 // 3️⃣ DELETE – service throws business rule (IllegalStateException)
    @Test
    @DisplayName("DELETE throws IllegalStateException → redirect back with flash error")
    void testDelete_BusinessException() throws Exception {

        // ❌ was: when(service.delete(anyLong())).thenThrow(new IllegalStateException("Cannot delete this agent"));
        // ✅ correct for void method:
        doThrow(new IllegalStateException("Cannot delete this agent"))
                .when(service).delete(anyLong());

        mockMvc.perform(get("/admin/agents/delete/5")
                .header(HttpHeaders.REFERER, "/admin/agents"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/agents"))
                .andExpect(flash().attribute("error", "Cannot delete this agent"));
    }
    // 4️⃣ UNEXPECTED ERROR — handled by GlobalExceptionHandler
    @Test
    @DisplayName("Unexpected error → redirect to /admin/restaurants with generic error")
    void testUnexpectedException() throws Exception {

        when(service.getAll()).thenThrow(new RuntimeException("DB down"));

        mockMvc.perform(get("/admin/agents"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/restaurants"))
                .andExpect(flash().attribute("error",
                        "We couldn’t complete that action. Please try again."));
    }
}